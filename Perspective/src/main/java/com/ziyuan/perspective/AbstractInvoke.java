/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractInvoke
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public abstract class AbstractInvoke implements Invoke {

    /**
     * 这个invoke的name(推荐以这次调用的方法名为name)
     */
    private final String name;

    /**
     * 一个invoke的持续时间
     */
    private long duration;

    /**
     * 标识自己所在的Branch的Id
     */
    private String ownerId;

    /**
     * 这是个全局Id,标识这个invoke属于哪个trace
     */
    private String traceId;

    /**
     * 默认一个长度为0的copyOnWrite的List
     */
    private List<Branch> CHILD_BRANCHES;

    /**
     * 子分支的数量
     */
    private final AtomicInteger INDEX = new AtomicInteger(0);

    /**
     *
     */
    private InvokeState state;

    /**
     * 错误
     */
    private Throwable t;

    protected AbstractInvoke(String name) {
        this.name = name;
        this.state = InvokeState.TRACING;
    }

    public String getName() {
        return this.name;
    }

    public long getDuration() {
        return this.duration;
    }

    public InvokeState getState() {
        return this.state;
    }

    public Invoke belongsTo() {
        return null;
    }

    public Branch newChildBranch(Invoke invoke) {
        if (CHILD_BRANCHES == null) {
            synchronized (this) {
                if (CHILD_BRANCHES == null) {
                    CHILD_BRANCHES = new CopyOnWriteArrayList<Branch>();
                }
            }
        }
        //TODO 这里还有两步: 1.放入一个新的starter 2.拿出Trace的Invoke num并+1 跟MAX_INVOKE_NODES比较
        return null;
    }

    public boolean isStarter() {
        return false;
    }

    public boolean isEnder() {
        return false;
    }

    public boolean finish() {
        return this.state.getValue() <= InvokeState.OVER.getValue();
    }

    public boolean isSuccess() {
        return this.state == InvokeState.OVER;
    }

    public void setError(Throwable t) {
        this.t = t;
        if (t instanceof TimeoutException) {
            this.state = InvokeState.TIMEOUT;
        } else {
            this.state = InvokeState.ERROR;
        }
    }

    public abstract String format();
}
