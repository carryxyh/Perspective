/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.util;

import com.ziyuan.perspective.Branch;
import com.ziyuan.perspective.Storage;
import com.ziyuan.perspective.Trace;
import com.ziyuan.perspective.storages.MemoryStorage;

/**
 * StorageUtil
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class StorageUtil {

    private static final Storage proxy = MemoryStorage.getInstance();

    public static Trace findTraceById(String traceId) {
        return proxy.findTraceById(traceId);
    }

    public static Branch findBranch(String traceId, String branchId) {
        return proxy.findBranch(traceId, branchId);
    }
}
