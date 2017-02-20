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
public class Ender extends SymbolInvoke {

    /**
     * 一个branch的结束时间
     */
    private long timestamp;

    private Throwable t;

    protected Ender(String name) {
        super(name);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Throwable getError() {
        return t;
    }

    public void setError(Throwable t) {
        this.t = t;
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
