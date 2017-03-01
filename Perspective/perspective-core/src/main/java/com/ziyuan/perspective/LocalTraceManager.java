/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.Exception.LocalTraceManagerClosedException;
import com.ziyuan.perspective.invokes.Trace;

/**
 * LocalTraceManager threadLocal管理器，单例
 * 考虑废弃掉,统一放到storage处理
 *
 * @author ziyuan
 * @since 2017-02-21
 */
@Deprecated
public final class LocalTraceManager {

    private volatile boolean isClosed;

    private ThreadLocal<Trace> LOCAL_MANAGER = new ThreadLocal<Trace>();

    public Trace get() throws Exception {
        if (isClosed) {
            throw new LocalTraceManagerClosedException();
        }
        return LOCAL_MANAGER.get();
    }

    /**
     * api层调用结束之后需要remove掉
     *
     * @throws Exception
     */
    public void remove() throws Exception {
        if (isClosed) {
            throw new LocalTraceManagerClosedException();
        }
        LOCAL_MANAGER.remove();
    }

    public void add(Trace trace) throws Exception {
        if (isClosed) {
            throw new LocalTraceManagerClosedException();
        }
        LOCAL_MANAGER.set(trace);
    }

    public void close() {
        if (isClosed) {
            return;
        }
        isClosed = true;
        //help gc
        LOCAL_MANAGER = null;
    }

    /**
     * 单例
     */
    private LocalTraceManager() {
    }

    private static final LocalTraceManager localTraceManager = new LocalTraceManager();

    public static LocalTraceManager getManager() {
        return localTraceManager;
    }
}
