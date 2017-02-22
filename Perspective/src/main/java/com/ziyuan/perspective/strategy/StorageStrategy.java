/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * StorageStrategy
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public interface StorageStrategy {

    /**
     * 存储一个trace，如果返回true，存，false不存
     *
     * @param trace 需要处理的trace
     * @return 是否存储
     */
    boolean storeTrace(Trace trace);
}
