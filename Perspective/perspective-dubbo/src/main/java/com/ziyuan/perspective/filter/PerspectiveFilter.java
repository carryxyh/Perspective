/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.ziyuan.perspective.LocalManager;
import com.ziyuan.perspective.invokes.Branch;
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

    /**
     * storage 的引用
     */
    private Storage storage = StorageUtil.getStorage();

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();

        Trace centreTrace = getCentreTrace();

        boolean isProvider = context.isProviderSide();
        if (isProvider) {


        } else {

        }

        return null;
    }

    /**
     * 获取当前的trace
     *
     * @return 当前的trace
     */
    private Trace getCentreTrace() {
        RpcContext context = RpcContext.getContext();
        Trace trace = StorageUtil.findTraceById(context.getAttachment(P_CENTRE_TRACE_ID));
        return trace;
    }

    /**
     * 获取当前线程的branch
     *
     * @return 当前线程的branch
     */
    private Branch getOwnerBranch() {
        Branch branch = LocalManager.getManager().getOwnBranch();
        return branch;
    }
}
