/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.io.Serializable;

/**
 * Invoke 某一次调用
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public interface Invoke extends Serializable, Formatable {

    /**
     * invoke nodes的最大数量,防止递归调用死循环
     */
    int MAX_INVOKE_NODES = Integer.MAX_VALUE >> 15;

    String getName();

    long getDuration();

    InvokeState getState();

    Invoke belongsTo();

    void newChildBranch(Invoke invoke);

    boolean isStarter();

    boolean isEnder();

    boolean ended();

    boolean isSuccess();

    void setState(InvokeState state);

    void setError(Throwable t);

    enum InvokeState {

        /**
         * 正在追踪
         */
        TRACING(1),

        /**
         * 追踪结束
         */
        OVER(2),

        /**
         * 异常
         */
        ERROR(3),

        /**
         * 超时
         */
        TIMEOUT(4);

        int value;

        InvokeState(int value){
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
