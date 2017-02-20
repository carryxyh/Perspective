/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * ArgInvoke
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public abstract class ArgInvoke extends AbstractInvoke {

    /**
     * 参数
     */
    private Object[] args;

    protected ArgInvoke(String name) {
        super(name);
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
