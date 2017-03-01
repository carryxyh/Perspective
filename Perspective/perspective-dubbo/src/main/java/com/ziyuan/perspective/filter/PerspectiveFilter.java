/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.ziyuan.perspective.Exception.SymbolIdEmptyException;
import com.ziyuan.perspective.LocalTraceManager;
import com.ziyuan.perspective.invokes.Invoke;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;

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

    private Storage storage = StorageUtil.getStorage();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean isFirstTier = isInitTier();
        String traceId = getCentreTraceId(isFirstTier);
        if (StringUtils.isBlank(traceId)) {
            //TODO 这里需要记录日志为什么拿不到TraceId
            return invoker.invoke(invocation);
        }

        Invoke ownerInvoke;
        if (isFirstTier) {
            //第一层
            try {
                ownerInvoke = LocalTraceManager.getManager().get();
            } catch (Exception e) {

            }
        } else {
            String branchId = invocation.getAttachment(P_OWN_BRANCH_ID);
            try {
                ownerInvoke = storage.findBranch(traceId, branchId);
            } catch (SymbolIdEmptyException e) {
                //TODO log
                return invoker.invoke(invocation);
            }
        }

        RpcContext context = RpcContext.getContext();
        //false说明世consumer side
        boolean isProvider = context.isProviderSide();
        if (isProvider) {
            //provider需要把invocation中的两个Id存下来 以便后面使用
            context.setAttachment(P_CENTRE_TRACE_ID, traceId);
            context.setAttachment(P_OWN_BRANCH_ID, invocation.getAttachment(P_OWN_BRANCH_ID));
        } else {
            //这里需要开启一个branch 把这个branch存入owner branch中

        }

        Result result;
        try {
            result = invoker.invoke(invocation);
        } catch (RpcException e) {
            //添加ender
            throw e;
        }
        return result;
    }

    /**
     * 获得traceId
     *
     * @return traceId
     */
    private String getCentreTraceId(boolean isFirstTier) {
        if (isFirstTier) {
            LocalTraceManager localTraceManager = LocalTraceManager.getManager();
            Trace trace = null;
            try {
                trace = localTraceManager.get();
            } catch (Exception e) {
                // ignore
            }
            return trace.getTraceId();
        } else {
            //说明这里已经不是API层了,从中央存储里取Id
            RpcContext context = RpcContext.getContext();
            String traceId = context.getAttachment(P_CENTRE_TRACE_ID);
            return traceId;
        }
    }

    /**
     * 如果是第一层，可以从中拿到trace 如果不是 threadlocal失效拿不到
     *
     * @return 是否是第一层（一般是API层）
     */
    private boolean isInitTier() {
        LocalTraceManager localTraceManager = LocalTraceManager.getManager();
        Trace trace = null;
        try {
            trace = localTraceManager.get();
        } catch (Exception e) {
            //TODO 记录日志
        }
        return trace != null;
    }
}
