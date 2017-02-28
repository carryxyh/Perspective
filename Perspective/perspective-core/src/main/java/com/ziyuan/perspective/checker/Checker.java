/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.checker;

import java.io.Serializable;

/**
 * Checker 用于检查storage中trace的状态
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public interface Checker extends Serializable, Cloneable {

    /**
     * 开始检查
     */
    void checkBegin();

    /**
     * 结束检查
     */
    void shutDown();
}
