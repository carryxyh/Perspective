/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Branch
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class Branch {

    private final ConcurrentLinkedQueue<Invoke> invokes = new ConcurrentLinkedQueue<Invoke>();

    public Branch(){
        //TODO 这里要做初始化一个starter,并且初始化starter的开始时间,放到invokes里
    }

    public void addInvoke(Invoke invoke) {
        this.invokes.add(invoke);
    }
}
