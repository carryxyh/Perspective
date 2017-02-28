/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Invoke;
import com.ziyuan.perspective.invokes.Trace;

/**
 * TimeOutCollectStrategy
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public final class TimeOutCollectStrategy implements CollectStrategy {

    /**
     * 只收集超时的trace
     */
    public boolean collectTrace(Trace trace) {
        return trace.getState() == Invoke.InvokeState.TIMEOUT;
    }
}
