/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * InvokeNode TODO ignore 暂时在每个调用节点上不记录参数
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class InvokeNode extends AbstractCollectionInvoke {

    /**
     * 拥有这个node的branchId
     */
    private final String ownerBranchId;

    protected InvokeNode(String name, String traceId, String ownerBranchId) {
        super(name, traceId);
        this.ownerBranchId = ownerBranchId;
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
