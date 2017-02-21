/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * Ender
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Ender extends InvokeNode {

    /**
     * 一个branch的结束时间
     */
    private long endTime;

    protected Ender(String name, String traceId, String ownerBranchId) {
        super(name, traceId, ownerBranchId);
        endTime = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return endTime;
    }

    @Override
    public String format() {
        return "";
    }
}
