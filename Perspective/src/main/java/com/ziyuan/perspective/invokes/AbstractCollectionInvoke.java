/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.Exception.InvokeNumsException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AbstractCollectionInvoke
 *
 * @author ziyuan
 * @since 2017-02-21
 */
public abstract class AbstractCollectionInvoke extends AbstractInvoke {

    /**
     * 控制一个trace中，除了starter之外的invokeNode数量，超过这个值认为死循环
     */
    private final AtomicInteger invokeNodeNum = new AtomicInteger();

    /**
     * 默认一个长度的copyOnWrite的List
     */
    protected List<Branch> CHILD_BRANCHES = new CopyOnWriteArrayList<Branch>();

    /**
     * 子分支的数量
     */
    private final AtomicInteger CHILD_BRANCH_NUM = new AtomicInteger(0);

    /**
     * 结束的子分支数量
     */
    private final AtomicInteger END_BRANCH_NUM = new AtomicInteger(0);

    protected AbstractCollectionInvoke(String name, String traceId) {
        super(name, traceId);
    }

    public void newChildBranch(Branch branch) throws Exception {
        if (this.finished()) {
            throw new IllegalStateException("Trace has finished !");
        }

        //如果一个trace中的branch数量超过了上限，抛异常并结束这个Trace
        if (Invoke.MAX_BRANCH_NODES < this.increaseAndGetChildBranchNum()) {
            Exception ex = new InvokeNumsException();
            this.setState(InvokeState.ERROR);
            this.setError(ex);
            this.decreaseAndGetChildBranchNum();
            throw ex;
        }
        if (branch != null) {
            this.increaseAndGetChildBranchNum();
            this.CHILD_BRANCHES.add(branch);
        }
    }

    /**
     * CHILD_BRANCHES中的第一个在创建时就已经放入了，name : traceName-main branchId : traceId-1
     *
     * @return
     */
    public Branch getMainBranch() {
        return this.CHILD_BRANCHES.get(0);
    }

    public int increaseAndGetInvokeNum() {
        return this.invokeNodeNum.incrementAndGet();
    }

    public int increaseAndGetChildBranchNum() {
        return CHILD_BRANCH_NUM.incrementAndGet();
    }

    public int decreaseAndGetChildBranchNum(){
        return CHILD_BRANCH_NUM.decrementAndGet();
    }

    public int getChildBranchNum() {
        return CHILD_BRANCH_NUM.get();
    }

    public int increaseAndGetEndBranchNum() {
        return END_BRANCH_NUM.incrementAndGet();
    }
}
