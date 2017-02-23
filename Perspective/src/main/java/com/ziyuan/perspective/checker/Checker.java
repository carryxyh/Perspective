/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.checker;

import java.io.Serializable;

/**
 * Checker
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public interface Checker extends Serializable, Cloneable {

    void checkBegin();

    /**
     * 结束检查
     */
    void shutDown();
}
