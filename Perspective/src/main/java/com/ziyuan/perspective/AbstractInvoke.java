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

    protected AbstractInvoke(String name) {
        this.name = name;
        this.state = InvokeState.TRACING;
    }

    @Override
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

    @Override
    public void endOneBranch(String branchId, Ender ender) {
        Trace trace = StorageUtil.findTraceById(this.traceId);
        if (trace.finished()) {
            //trace已经结束了，则不改变状态，只添加
            return;
        }
        Branch branch = trace.getOneBranch(branchId);
        branch.setEnder(ender);
        if (this.END_BRANCH_NUM.incrementAndGet() == CHILD_BRANCH_NUM.get()) {
            //相等，所有子分支结束了,根据这个ender的成功失败给整个trace设置成功失败
            for (Branch child : this.CHILD_BRANCHES) {
                if (!child.isSuccess()) {
                    this.setState(InvokeState.ERROR);
                    this.setError(child.getError());
                    break;
                }
            }
        } else {
            //存在没结束的子分支，不改变整个trace的状态
        }
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public abstract String format();

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public long getDuration() {
        return this.duration;
    }

    @Override
    public InvokeState getState() {
        return this.state;
    }

    @Override
    public void setState(InvokeState state) {
        this.state = state;
    }

    @Override
    public Branch belongsTo() {
        return this.ownerBranch;
    }

    @Override
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

    @Override
    public boolean finished() {
        return this.state.getValue() <= InvokeState.OVER.getValue();
    }

    @Override
    public boolean isSuccess() {
        return this.state == InvokeState.OVER;
    }
}
