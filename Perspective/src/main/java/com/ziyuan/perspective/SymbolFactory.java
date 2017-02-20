/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.util.UUID;

/**
 * SymbolFactory,用于生成ID的工厂类
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class SymbolFactory {

    private static final UUID UUID_TOOL = UUID.randomUUID();

    public static String createTraceId(){
        return UUID_TOOL.toString();
    }
}
