/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * ErrorCollectStrategy
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public final class ErrorCollectStrategy implements CollectStrategy {

    /**
     * 如果不成功，收集
     *
     * @param trace trace
     * @return 是否收集
     */
    public boolean collectTrace(Trace trace) {
        return !trace.isSuccess();
    }
}
