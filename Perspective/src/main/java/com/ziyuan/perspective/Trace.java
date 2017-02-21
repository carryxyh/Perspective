/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Trace
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Trace extends ArgInvoke {

    /**
     * 控制一个trace中，除了starter之外的invokeNode数量，超过这个值认为死循环
     */
    private final AtomicInteger invokeNodeNum = new AtomicInteger();

    /**
     * 控制一个trace钟，branch的数量
     */
    private final AtomicInteger branchNum = new AtomicInteger();

    /**
     * 所有branch的key - value结构，用于快速找到一个branch
     */
    private Map<String, Branch> allBranches = new ConcurrentHashMap<String, Branch>(16);

    protected Trace(String name, String traceId) {
        super(name, traceId);
    }

    @Override
    public Branch belongsTo() {
        //trace的parent为null
        return null;
    }

    @Override
    public String format() {
        //todo
        return null;
    }

    public String getTraceId() {
        return super.getTraceId();
    }

    /**
     * 放入一个branch，branch的数量（异步线程的数量）不得超过31个
     *
     * @param branchId branchId
     * @param branch   branch
     */
    public void addOneBranch(String branchId, Branch branch) throws Exception {
        if (super.finished()) {
            throw new IllegalStateException("Trace has finished !o");
        }

        //如果一个trace中的branch数量超过了上限，抛异常并结束这个Trace
        if (Invoke.MAX_BRANCH_NODES < this.branchNum.incrementAndGet()) {
            Exception ex = new Exception("Branch num is more than the max_branch_nodes !");
            this.setState(InvokeState.ERROR);
            this.setError(ex);
            throw ex;
        }
        Branch b = allBranches.get(branchId);
        if (b == null) {
            allBranches.put(branchId, branch);
        }
    }

    public Branch getOneBranch(String branchId) {
        return allBranches.get(branchId);
    }

    public int increaseAndGetInvokeNum() {
        return this.invokeNodeNum.incrementAndGet();
    }

    public int increaseAndGetBranchNum() {
        return this.branchNum.incrementAndGet();
    }
}
