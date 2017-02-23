/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.Constants;
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
        String branchId = traceId + "-" + this.getAndIncreaseChildBranchNum();
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
        StringBuffer sb = new StringBuffer("");
        if (this.isSuccess()) {
            //TODO 这里以后完善，现在只收集失败的trace
        } else {
            //失败的
            sb.append("Trace error : {").append("Trace name -> ").append(this.getName()).append(", ").append("error branches is : [ \n");
            for (Branch b : errorBranches) {
                sb.append(b.getName()).append(", the error is : ").append(b.getError()).append("\n");
            }
            sb.append("] \n").append("the break reason is -> ").append(this.getError().getMessage()).append("}");
        }
        return sb.toString();
    }

    public Branch getOneBranch(String branchId) {
        return allBranches.get(branchId);
    }

    /**
     * 为这个trace放入一个branch
     *
     * @param branch branch
     */
    public void putBranch(Branch branch) {
        if (branch != null) {
            allBranches.put(branch.getBranchId(), branch);
        }
    }

    /**
     * 获取错误的branch集合
     *
     * @return 错误的branch集合
     */
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
        b.addEnder(ender);
        if (this.finished()) {
            if (this.increaseAndGetEndBranchNum() == this.getChildBranchNum()) {
                if (!ender.isSuccess()) {
                    errorBranches.add(b);
                } else {
                    long duration = ender.getTimestamp() - this.getStartTime();
                    if (duration > Constants.TIME_OUT) {
                        //成功但是超时了
                        b.setState(InvokeState.TIMEOUT);
                        b.setDuration(duration);
                        errorBranches.add(b);
                    }
                }
                StorageUtil.endOneTrace(this);
            }
            return;
        }

        long duration = ender.getTimestamp() - this.getStartTime();
        this.setDuration(duration);
        if (ender.isSuccess()) {
            if (duration < Constants.TIME_OUT) {
                this.setState(InvokeState.OVER);
            } else {
                this.setState(InvokeState.TIMEOUT);
                errorBranches.add(b);
            }
        } else {
            //不成功，直接结束一个trace
            this.setError(ender.getError());
            this.setState(ender.getState());
            this.errorBranches.add(b);
        }
        if (this.increaseAndGetEndBranchNum() == this.getChildBranchNum()) {
            StorageUtil.endOneTrace(this);
        }
    }

    /**
     * 检查自己是否超时
     *
     * @param timestamp 时间点
     */
    public void checkTimeOut(long timestamp) {

    }
}
