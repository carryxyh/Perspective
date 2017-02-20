/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * SymbolInvoke
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public abstract class SymbolInvoke extends AbstractInvoke {

    protected SymbolInvoke(String name) {
        super(name);
    }

    public boolean isStarter() {
        return false;
    }

    public boolean isEnder() {
        return false;
    }
}
