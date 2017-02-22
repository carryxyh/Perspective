/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.Exception.LocalTraceManagerClosedException;
import com.ziyuan.perspective.invokes.Trace;

/**
 * LocalTraceManager threadLocal管理器，单例
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public final class LocalTraceManager {

    private volatile boolean isClosed;

    private ThreadLocal<Trace> LOCAL_MANAGER = new ThreadLocal<Trace>();

    private LocalTraceManager() {
    }

    private static final LocalTraceManager localTraceManager = new LocalTraceManager();

    public LocalTraceManager getInstance() {
        return localTraceManager;
    }

    public Trace get() throws Exception {
        if (isClosed) {
            throw new LocalTraceManagerClosedException();
        }
        return LOCAL_MANAGER.get();
    }

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
}
