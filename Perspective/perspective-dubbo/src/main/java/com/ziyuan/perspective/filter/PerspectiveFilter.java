/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.ziyuan.perspective.LocalManager;
import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Ender;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;

import java.util.Map;

/**
 * PerspectiveInitFilter perspective的filter
 *
 * @author ziyuan
 * @since 2017-02-28
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER}, order = -10001)
public class PerspectiveFilter implements Filter {

    /**
     * 中央traceId的键
     */
    private static final String P_CENTRE_TRACE_ID = "centre_trace_id";

    /**
     * owner branch的键
     */
    private static final String P_BRANCH_ID = "own_branch_id";

    /**
     * storage 的引用
     */
    private Storage storage = StorageUtil.getStorage();

    /**
     * local manager 的引用
     */
    private LocalManager localManager = LocalManager.getManager();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext context = RpcContext.getContext();

        Trace centreTrace = getCentreTrace();

        boolean isProvider = context.isProviderSide();
        if (isProvider) {

            String ownerBranchId = invocation.getAttachment(P_BRANCH_ID);
            holdIds(context, centreTrace.getTraceId(), ownerBranchId);

            Branch owner = centreTrace.getOneBranch(ownerBranchId);
            Branch branch = new Branch(invoker.getUrl().getPath() + "." + invocation.getMethodName(), centreTrace.getTraceId(), owner);
            localManager.addBranch(branch);
            try {
                owner.addInvoke(branch);
            } catch (Exception e) {
                throw new RpcException(e.getMessage(), e);
            }

            Result result;
            Ender ender = new Ender(branch.getName() + "- end", centreTrace.getTraceId(), branch.getBranchId());
            try {
                result = invoker.invoke(invocation);
            } catch (RpcException rpce) {
                ender.setError(rpce);
                throw rpce;
            } finally {
                centreTrace.endOneBranch(branch.getBranchId(), ender);
            }
            return result;
        } else {

            Branch ownerBranch = localManager.getOwnBranch();
            Map<String, String> attachments = invocation.getAttachments();
            //Id传到下一级
            attachments.put(P_BRANCH_ID, ownerBranch.getBranchId());
            Result result = invoker.invoke(invocation);
            return result;
        }
    }

    /**
     * 保存Id
     *
     * @param context       rpc上下文
     * @param traceId       traceId
     * @param ownerBranchId branchId
     */
    private void holdIds(RpcContext context, String traceId, String ownerBranchId) {
        context.setAttachment(P_CENTRE_TRACE_ID, traceId);
        context.setAttachment(P_BRANCH_ID, ownerBranchId);
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
}
