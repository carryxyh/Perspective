/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.Exception.InvokeNumsException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Trace
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Trace extends AbstractCollectionInvoke {

    /**
     * 所有branch的key - value结构，用于快速找到一个branch
     */
    private Map<String, Branch> allBranches = new ConcurrentHashMap<String, Branch>(16);

    /**
     * 有问题的branch集合
     */
    private Set<Branch> errorBranches = new CopyOnWriteArraySet<Branch>();

    protected Trace(String name, String traceId) {
        super(name, traceId);
    }

    public void newChildBranch(Branch branch) throws Exception {
        if (this.finished()) {
            throw new IllegalStateException("Trace has finished !");
        }

        //如果一个trace中的branch数量超过了上限，抛异常并结束这个Trace
        if (Invoke.MAX_BRANCH_NODES < this.increaseAndGetBranchNum()) {
            Exception ex = new InvokeNumsException();
            this.setState(InvokeState.ERROR);
            this.setError(ex);
            throw ex;
        }
        Branch b = allBranches.get(branch.getBranchId());
        if (b == null) {
            allBranches.put(branch.getBranchId(), branch);
            this.increaseAndGetChildBranchNum();
        }
    }

    @Override
    public String format() {
        //todo
        return null;
    }

    public String getTraceId() {
        return super.getTraceId();
    }

    public Branch getOneBranch(String branchId) {
        return allBranches.get(branchId);
    }

    public Set<Branch> getErrorBranch() {
        return errorBranches;
    }

    /**
     * 根据这个branchId 结束一个branch
     *
     * @param branchId branch id
     * @param ender    ender
     */
    public void endOneBranch(String branchId, Ender ender) {
        Branch b = allBranches.get(branchId);
        if (b == null) {
            return;
        }
        if (b.finished()) {
            return;
        }

        if (!ender.isSuccess()) {
            this.setError(ender.getError());
            this.setState(ender.getState());
            this.errorBranches.add(b);
            this.increaseAndGetEndBranchNum();
            this.setDuration(this.getStartTime() - ender.getTimestamp());
        } else {
            if (this.increaseAndGetEndBranchNum() == this.getChildBranchNum()) {
                this.setState(InvokeState.OVER);
                this.setDuration(this.getStartTime() - ender.getTimestamp());
            }
        }
        b.addEnder(ender);
    }
}
