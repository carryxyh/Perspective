/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.ziyuan.perspective.LocalTraceManager;
import com.ziyuan.perspective.invokes.Trace;

/**
 * PerspectiveFilter perspective的filter
 *
 * @author ziyuan
 * @since 2017-02-28
 */
@Activate(group = {"provider", "consumer"}, order = -10001)
public class PerspectiveFilter implements Filter {

    /**
     * 中央traceId的键
     */
    private static final String P_CENTRE_TRACE_ID = "centre_trace_id";

    /**
     * owner branch的键
     */
    private static final String P_OWN_BRANCH_ID = "own_branch_id";

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        String traceId = getCentreTraceId();
        if (StringUtils.isBlank(traceId)) {
            //TODO 这里需要记录日志为什么拿不到TraceId
            return invoker.invoke(invocation);
        }

        String branchId = getOwnBranchId();

        //false说明世consumer side
        boolean isProvider = RpcContext.getContext().isProviderSide();

        Result result = invoker.invoke(invocation);
        return result;
    }

    /**
     * 获得traceId
     *
     * @return traceId
     */
    private String getCentreTraceId() {
        LocalTraceManager localTraceManager = LocalTraceManager.getManager();
        Trace trace = null;
        try {
            trace = localTraceManager.get();
        } catch (Exception e) {
            //TODO 记录日志
        }
        if (trace == null) {
            //说明这里已经不是API层了,从中央存储里取Id
            RpcContext context = RpcContext.getContext();
            String traceId = context.getAttachment(P_CENTRE_TRACE_ID);
            return traceId;
        }
        return trace.getTraceId();
    }

    /**
     * 获得branchId
     *
     * @return branchId
     */
    private String getOwnBranchId() {
        RpcContext context = RpcContext.getContext();
        String branchId = context.getAttachment(P_OWN_BRANCH_ID);
        return branchId;
    }
}
