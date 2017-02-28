/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

/**
 * Ender 标示着一个branch的结束（这里包括正常和异常结束）
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Ender extends AbstractInvoke {

    /**
     * 一个branch的结束时间
     */
    private long endTime;

    private String ownerBranchId;

    public Ender(String name, String traceId, String ownerBranchId) {
        super(name, traceId);
        endTime = System.currentTimeMillis();
        //默认的ender就是正常结束的
        this.setState(InvokeState.OVER);
        this.ownerBranchId = ownerBranchId;
    }

    @Override
    public void setError(Throwable error) {
        super.setError(error);
        this.setState(InvokeState.ERROR);
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public String format() {
        return "";
    }
}
