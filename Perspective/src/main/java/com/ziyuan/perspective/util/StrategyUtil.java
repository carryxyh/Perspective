/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.util;

import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.strategy.ErrorStorageStrategy;
import com.ziyuan.perspective.strategy.StorageStrategy;

/**
 * StrategyUtil 主要用来把storage、collector和strategy解耦
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public final class StrategyUtil {

    /**
     * 存储策略
     */
    public static StorageStrategy storageStrategy = new ErrorStorageStrategy();

    /**
     * 过滤一个需要存储的trace
     *
     * @param trace trace
     * @return 是否需要存储
     */
    public static boolean filterStorage(Trace trace) {
        return storageStrategy.storeTrace(trace);
    }
}
