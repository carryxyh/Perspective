/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * AllCollectStrategy
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public final class AllCollectStrategy implements CollectStrategy {

    public boolean collectTrace(Trace trace) {
        return true;
    }
}
