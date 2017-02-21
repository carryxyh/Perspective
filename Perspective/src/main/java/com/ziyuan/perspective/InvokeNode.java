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
public class InvokeNode extends AbstractInvoke {

    protected InvokeNode(String name, String traceId) {
        super(name, traceId);
    }

    @Override
    public String format() {
        return null;
    }
}
