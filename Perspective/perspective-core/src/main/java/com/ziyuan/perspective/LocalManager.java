/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.invokes.InvokeNode;
import com.ziyuan.perspective.invokes.Trace;

/**
 * LocalManager threadLocal管理器，单例
 * 考虑废弃掉,统一放到storage处理
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public final class LocalManager {

    private ThreadLocal<Trace> LOCAL_MANAGER = new ThreadLocal<Trace>();

    private ThreadLocal<InvokeNode> LOCAL_NODE = new ThreadLocal<>();

    public Trace get() throws Exception {
        return LOCAL_MANAGER.get();
    }

    /**
     * api层调用结束之后需要remove掉
     *
     * @throws Exception
     */
    public void remove() throws Exception {
        LOCAL_MANAGER.remove();
    }

    public void add(Trace trace) throws Exception {
        LOCAL_MANAGER.set(trace);
    }

    public InvokeNode getLocalNode() {
        return LOCAL_NODE.get();
    }

    public void removeNode() {
        LOCAL_NODE.remove();
    }

    public void addNode(InvokeNode invokeNode) {
        LOCAL_NODE.set(invokeNode);
    }

    /**
     * 单例
     */
    private LocalManager() {
    }

    private static final LocalManager localTraceManager = new LocalManager();

    public static LocalManager getManager() {
        return localTraceManager;
    }
}
