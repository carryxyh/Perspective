/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.Exception;

/**
 * SymbolIdEmptyException
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public class SymbolIdEmptyException extends Exception {

    public SymbolIdEmptyException() {
        super("TraceId or BranchId is null/blank !");
    }
}
