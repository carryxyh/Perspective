/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.util.StorageUtil;

/**
 * InvokeNode TODO ignore 暂时在每个调用节点上不记录参数
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class InvokeNode extends AbstractCollectionInvoke {

    /**
     * 拥有这个node的branchId
     */
    private final String ownerBranchId;

    public InvokeNode(String name, String traceId, String ownerBranchId) {
        super(name, traceId);

        //每一个node都有一个主branch
        String branchId = traceId + "-" + this.getAndIncreaseChildBranchNum();
        Branch b = new Branch(name + "-main", traceId, branchId, this);

        //放到trace中方便查找
        Trace t = StorageUtil.findTraceById(this.getTraceId());
        t.putBranch(b);

        super.CHILD_BRANCHES.add(b);
        this.ownerBranchId = ownerBranchId;
        t.increaseAndGetChildBranchNum();
    }

    @Override
    public String format() {
        return null;
    }

    public String getOwnerBranchId() {
        return ownerBranchId;
    }

    @Override
    public void setArgs(Object... obj) {
        //这里现在默认不记录参数
    }
}
