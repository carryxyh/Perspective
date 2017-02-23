/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * CollectStrategy
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public interface CollectStrategy extends Strategy {

    /**
     * 手机trace的策略
     *
     * @param trace trace
     * @return 是否收集
     */
    boolean collectTrace(Trace trace);
}
