/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.util.StorageUtil;

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
        String branchId = traceId + "-" + this.increaseAndGetChildBranchNum();
        Branch b = new Branch(name + "-main", traceId, branchId, this);
        super.CHILD_BRANCHES.add(b);
        this.allBranches.put(branchId, b);
        StorageUtil.newTrace(this);
    }

    public void newChildBranch(Branch branch) throws Exception {
        super.newChildBranch(branch);
        allBranches.put(branch.getBranchId(), branch);
    }

    @Override
    public String format() {
        //todo
        return null;
    }

    public Branch getOneBranch(String branchId) {
        return allBranches.get(branchId);
    }

    public void putBranch(Branch branch) {
        if (branch != null) {
            allBranches.put(branch.getBranchId(), branch);
        }
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
        if (this.finished()) {
            if (this.increaseAndGetEndBranchNum() == this.getChildBranchNum()) {
                StorageUtil.endOneTrace(this);
            }
            return;
        }

        if (ender.isSuccess()) {
            //todo 这里根据超时时间来判断，状态应该设置为超时还是正常结束
            this.setDuration(ender.getTimestamp() - this.getStartTime());
            this.setState(InvokeState.OVER);
        } else {
            //不成功，直接结束一个trace
            this.setError(ender.getError());
            this.setState(ender.getState());
            this.errorBranches.add(b);
            //todo
            this.setDuration(ender.getTimestamp() - this.getStartTime());
        }
        if (this.increaseAndGetEndBranchNum() == this.getChildBranchNum()) {
            StorageUtil.endOneTrace(this);
        }
        b.addEnder(ender);
    }
}
