/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.storages;

import com.ziyuan.perspective.Storage;
import com.ziyuan.perspective.Trace;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MemoryStorage
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class MemoryStorage implements Storage {

    private static final ThreadLocal<Trace> LOCAL = new ThreadLocal<Trace>();

    private static final Map<String, Trace> TRACE_MAP = new ConcurrentHashMap<String, Trace>();

    private static final MemoryStorage memoryStorage = new MemoryStorage();

    private MemoryStorage() {

    }

    public static MemoryStorage getInstance() {
        return memoryStorage;
    }

    public Trace getLocalTrace() {
        return LOCAL.get();
    }

    public Trace getLocalTrace(String traceId) {
        return TRACE_MAP.get(traceId);
    }
}
