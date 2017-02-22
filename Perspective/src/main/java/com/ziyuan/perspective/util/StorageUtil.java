/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.util;

import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.MemoryStorage;
import com.ziyuan.perspective.storages.Storage;

/**
 * StorageUtil storage的工具类，主要也是用来其他组件和storage的解耦
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class StorageUtil {

    /**
     * 真正的storage
     */
    private static Storage proxy = MemoryStorage.getInstance();

    /**
     * 通过traceId找到一个proxy
     *
     * @param traceId traceId
     * @return trace
     */
    public static Trace findTraceById(String traceId) {
        return proxy.findTraceById(traceId);
    }

    /**
     * 通过traceId找到一个trace，再从中找出一个branch
     *
     * @param traceId  traceId
     * @param branchId branchId
     * @return branch
     */
    public static Branch findBranch(String traceId, String branchId) {
        return proxy.findBranch(traceId, branchId);
    }

    /**
     * 结束一次追踪
     *
     * @param trace trace
     */
    public static void endOneTrace(Trace trace) {
        if (trace == null) {
            return;
        }
        proxy.endOneTrace(trace);
    }

    public static void newTrace(Trace trace) {
        if (trace == null) {
            return;
        }
        proxy.newTrace(trace);
    }

    /**
     * 返回storage对象的引用
     *
     * @return storage
     */
    public static Storage getStorage() {
        return proxy;
    }

    /**
     * 设置一个proxy
     *
     * @param proxy proxy
     */
    public static void setProxy(Storage proxy) {
        StorageUtil.proxy = proxy;
    }
}
