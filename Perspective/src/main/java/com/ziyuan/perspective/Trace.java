/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * Trace
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Trace extends ArgInvoke {

    private final String traceId;

    protected Trace(String name, String traceId) {
        super(name);
        this.traceId = traceId;
    }

    @Override
    public Invoke belongsTo() {
        return null;
    }

    @Override
    public String format() {
        return null;
    }

    public String getTraceId() {
        return traceId;
    }
}
