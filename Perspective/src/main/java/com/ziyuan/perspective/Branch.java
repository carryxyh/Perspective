/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Branch
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Branch extends ArgInvoke {

    private final ConcurrentLinkedQueue<Invoke> invokes = new ConcurrentLinkedQueue<Invoke>();

    /**
     * branchId 用于快速定位一个branch
     */
    private String branchId;

    /**
     * 标志一个starter的开始
     */
    private Starter starter;

    /**
     * 这个branch属于哪个invoke
     */
    private Invoke ownerInvoke;

    protected Branch(String name, String branchId, Invoke ownerInvoke) {
        super(name);
        this.ownerInvoke = ownerInvoke;
        this.branchId = branchId;
        this.starter = new Starter(name + "-starter");
        invokes.add(starter);
    }

    public void addInvoke(Invoke invoke) {
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
        return this.branchId;
    }

    /**
     * 给一个branch设置结束点，根据结束点的状态判断整个branch的状态
     *
     * @param ender
     */
    public void setEnder(Ender ender) {
        if (super.finished()) {
            //如果已经结束，不改变原有状态,直接加入之后返回
            invokes.add(ender);
            return;
        }
        if (ender.isSuccess()) {
            super.setState(InvokeState.OVER);
        } else {
            super.setError(ender.getError());
            //把拥有者设为失败
            ownerInvoke.setError(ender.getError());
        }
        super.duration = ender.getTimestamp() - this.starter.getStartTime();
        invokes.add(ender);
    }
}
