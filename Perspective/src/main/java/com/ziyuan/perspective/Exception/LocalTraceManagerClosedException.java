/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.Exception;

/**
 * LocalTraceManagerClosedException LocalTraceManager已经关闭的异常
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public class LocalTraceManagerClosedException extends Exception {

    public LocalTraceManagerClosedException() {
        super("LocalTraceManager has closed !");
    }
}
