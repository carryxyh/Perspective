/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.util.StorageUtil;

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
    protected long duration;

    /**
     * 标识自己所在的Branch的Id
     */
    private Branch ownerBranch;

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
    private final AtomicInteger CHILD_BRANCH_NUM = new AtomicInteger();

    private final AtomicInteger END_BRANCH_NUM = new AtomicInteger();

    /**
     * 状态
     */
    private InvokeState state;

    /**
     * 错误
     */
    private Throwable error;

    protected AbstractInvoke(String name, String traceId) {
        this.name = name;
        this.state = InvokeState.TRACING;
        this.traceId = traceId;
    }

    public void newChildBranch(Branch branch) {
        if (CHILD_BRANCHES == null) {
            synchronized (this) {
                if (CHILD_BRANCHES == null) {
                    CHILD_BRANCHES = new CopyOnWriteArrayList<Branch>();
                }
            }
        }
        Trace trace = StorageUtil.findTraceById(this.traceId);
        if (trace.increaseAndGetBranchNum() <= Invoke.MAX_BRANCH_NODES) {
            CHILD_BRANCHES.add(branch);
            CHILD_BRANCH_NUM.incrementAndGet();
        }
    }

    public void endOneBranch(String branchId, Ender ender) {
        Trace trace = StorageUtil.findTraceById(this.traceId);
        if (trace.finished()) {
            //trace已经结束了，则不改变状态，只添加
            return;
        }
        Branch branch = trace.getOneBranch(branchId);
        branch.setEnder(ender);
        if (this.END_BRANCH_NUM.incrementAndGet() == this.CHILD_BRANCH_NUM.get()) {
            //相等，所有子分支结束了,根据这个ender的成功失败给整个trace设置成功失败
            new Thread(new Runnable() {

                public void run() {
                    for (Branch child : CHILD_BRANCHES) {
                        if (child.finished() && !child.isSuccess()) {
                            setState(InvokeState.ERROR);
                            setError(child.getError());
                            break;
                        }
                    }
                }
            }).start();
        }
    }

    public String getTraceId() {
        return traceId;
    }

    public abstract String format();

    public String getName() {
        return this.name;
    }

    public long getDuration() {
        return this.duration;
    }

    public InvokeState getState() {
        return this.state;
    }

    public void setState(InvokeState state) {
        this.state = state;
    }

    public Branch belongsTo() {
        return this.ownerBranch;
    }

    public void setError(Throwable error) {
        this.error = error;
        if (error instanceof TimeoutException) {
            this.state = InvokeState.TIMEOUT;
        } else {
            this.state = InvokeState.ERROR;
        }
    }

    public Throwable getError() {
        return error;
    }

    public boolean finished() {
        return this.state.getValue() <= InvokeState.OVER.getValue();
    }

    public boolean isSuccess() {
        return this.state == InvokeState.OVER;
    }
}
