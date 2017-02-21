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
    int MAX_INVOKE_NODES = Integer.MAX_VALUE >> 21;

    int MAX_BRANCH_NODES = Integer.MAX_VALUE >> 25;

    /**
     * 获取一个invoke的名字
     *
     * @return invoke 名字
     */
    String getName();

    /**
     * 获取持续时间
     *
     * @return 持续时间
     */
    long getDuration();

    /**
     * 获取invoke的状态
     *
     * @return invoke状态
     */
    InvokeState getState();

    /**
     * 是否已经完成(成功/失败)
     *
     * @return 是否完成
     */
    boolean finished();

    /**
     * 是否成功
     *
     * @return 是否成功
     */
    boolean isSuccess();

    /**
     * 设置异常,如果超时，放入一个TimeOutException
     *
     * @param error 异常
     * @see java.util.concurrent.TimeoutException
     */
    void setError(Throwable error);

    /**
     * 属于哪个branch
     *
     * @return branch
     */
    Branch belongsTo();

    /**
     * 只在多线程的情况下会使用到
     */
    void newChildBranch(Branch branch);

    /**
     * 给一个invoke设置状态
     *
     * @param state state
     */
    void setState(InvokeState state);

    /**
     * 给其中的一个branch设置结束点
     *
     * @param ender
     * @param branchId
     */
    void endOneBranch(String branchId, Ender ender);

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

        InvokeState(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
