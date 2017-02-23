/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.Exception.InvokeNumsException;
import com.ziyuan.perspective.Exception.TraceNotFoundException;
import com.ziyuan.perspective.util.StorageUtil;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Branch
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Branch extends AbstractInvoke {

    /**
     * 这里允许放入InvokeNode和Branch
     */
    private final ConcurrentLinkedQueue<Invoke> invokes = new ConcurrentLinkedQueue<Invoke>();

    /**
     * branchId 用于快速定位一个branch,一个branchId在Trace中必须唯一
     */
    private String branchId;

    /**
     * 这个branch属于哪个invoke 只可能是InvokeNode和Trace
     */
    private Invoke ownerInvoke;

    public Branch(String name, String traceId, String branchId, Invoke ownerInvoke) {
        super(name, traceId);
        this.ownerInvoke = ownerInvoke;
        this.branchId = branchId;
    }

    public Branch(String name, String traceId, AbstractCollectionInvoke ownerInvoke) {
        super(name, traceId);
        this.ownerInvoke = ownerInvoke;
        String branchId = traceId + "-" + ownerInvoke.increaseAndGetChildBranchNum();
        this.branchId = branchId;
    }

    public void addInvoke(Invoke invoke) throws Exception {
        Trace trace = StorageUtil.findTraceById(super.getTraceId());
        if (trace == null) {
            throw new TraceNotFoundException();
        }
        if (trace.increaseAndGetInvokeNum() > Invoke.MAX_INVOKE_NODES) {
            throw new InvokeNumsException();
        }
        if (invoke instanceof Branch) {
            trace.putBranch((Branch) invoke);
            trace.increaseAndGetChildBranchNum();
        }
        this.invokes.add(invoke);
    }

    @Override
    public String format() {
        StringBuilder builder = new StringBuilder("BranchID : ");
        builder.append(branchId).append(", TraceId : ").append(super.getTraceId()).append(", Args : [");
        for (Object obj : super.getArgs()) {
            builder.append(obj).append(", ");
        }
        builder.append("]");
        return builder.toString();
    }

    public String getBranchId() {
        return branchId;
    }

    public Invoke getOwnerInvoke() {
        return ownerInvoke;
    }

    /**
     * 添加一个Ender，结束这个branch，这个方法调用方只能是trace！！！
     *
     * @param ender
     */
    public void addEnder(Ender ender) {
        //设置结束时间
        this.setDuration(ender.getTimestamp() - this.getStartTime());

        try {
            this.addInvoke(ender);
        } catch (Exception e) {
            super.setError(e);
            super.setState(InvokeState.ERROR);
            return;
        }

        if (ender.isSuccess()) {
            this.setState(InvokeState.OVER);
        } else {
            this.setError(ender.getError());
            this.setState(ender.getState());
        }
    }
}
