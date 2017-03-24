
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.Formatable;

import java.io.Serializable;

/**
 * Invoke 某一次调用
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public interface Invoke extends Serializable, Formatable, Comparable<Invoke> {

    /**
     * invoke nodes的最大数量,防止递归调用死循环
     */
    int MAX_INVOKE_NODES = Integer.MAX_VALUE >> 21;

    /**
     * invoke中最大的branch数量,防止线程过多
     */
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
     * 给一个invoke设置状态
     *
     * @param state state
     */
    void setState(InvokeState state);

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
     * 获得异常
     *
     * @return 异常
     */
    Throwable getError();

    /**
     * 设置参数
     *
     * @param obj 参数集合
     */
    void setArgs(Object... obj);

    /**
     * 获得参数
     *
     * @return 参数集合
     */
    Object[] getArgs();

    /**
     * invoke 状态的枚举
     */
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
