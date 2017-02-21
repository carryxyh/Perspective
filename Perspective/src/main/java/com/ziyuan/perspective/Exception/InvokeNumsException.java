/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.Exception;

/**
 * InvokeNumsException
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public class InvokeNumsException extends Exception {

    public InvokeNumsException() {
        super("Invoke nums is more than the MAX_INVOKE_NUMS !");
    }
}
