/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.storages;

import com.ziyuan.perspective.Branch;
import com.ziyuan.perspective.Storage;
import com.ziyuan.perspective.Trace;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MemoryStorage 内存存储
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class MemoryStorage implements Storage {

    private Map<String, Trace> storage = new ConcurrentHashMap<String, Trace>(128);

    private static final MemoryStorage instance = new MemoryStorage();

    private MemoryStorage() {

    }

    public static MemoryStorage getInstance() {
        return instance;
    }

    public Trace findTraceById(String traceId) {
        return storage.get(traceId);
    }

    public Branch findBranch(String traceId, String branchId) {
        Trace t = storage.get(traceId);
        if (t == null) {
            return null;
        }
        return t.getOneBranch(branchId);
    }
}
