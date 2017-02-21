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
public final class Ender extends SymbolInvoke {

    /**
     * 一个branch的结束时间
     */
    private long timestamp;

    protected Ender(String name, String traceId) {
        super(name, traceId);
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isEnder() {
        return true;
    }

    @Override
    public String format() {
        return "";
    }
}
