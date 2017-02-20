/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * Starter
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class Starter extends SymbolInvoke {

    /**
     * 一个branch的开始时间
     */
    private long timestamp;

    protected Starter(String name) {
        super(name);
        this.timestamp = System.currentTimeMillis();
    }

    public long getStartTime() {
        return this.timestamp;
    }

    @Override
    public boolean isStarter() {
        return true;
    }

    @Override
    public String format() {
        return "";
    }
}
