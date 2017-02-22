/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.invokes;

import com.ziyuan.perspective.LocalTraceManager;
import com.ziyuan.perspective.SymbolFactory;
import com.ziyuan.perspective.util.StorageUtil;
import junit.framework.TestCase;

/**
 * InvokeTest
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public class InvokeTest extends TestCase {

    public void testChain() throws Exception {

        LocalTraceManager localTraceManager = LocalTraceManager.getInstance();
        Trace trace = new Trace("weixin-meal", SymbolFactory.createTraceId());
        localTraceManager.add(trace);

        //weixin-meal main
        Branch mainT = trace.getMainBranch();

        /*-------------------到这里第一层开始-----------------------------*/

        InvokeNode invokeNode1 = new InvokeNode("weixin-meal 中的一个方法", localTraceManager.get().getTraceId(), mainT.getBranchId());
        mainT.addInvoke(invokeNode1);
        // weixin-method main
        Branch mainW = invokeNode1.getMainBranch();
        /*----------------------到这里第二层开始------------------------------*/

        String mainWId = mainW.getBranchId();

        System.out.println("未经过存储策略筛选过后，剩下的 ： " + StorageUtil.getStorage().getTracing().size());

        //模拟方法调用耗时
        Thread.sleep(100);

        /*--------------------------到这里第二层结束----------------------------*/
        trace.endOneBranch(mainWId, new Ender("weixin-meal 中的方法 over", trace.getTraceId(), mainWId));

        /*--------------------------到这里第一层结束----------------------------*/
        trace.endOneBranch(mainWId, new Ender("weixin-meal over", trace.getTraceId(), mainT.getBranchId()));

        System.out.println("trace 追踪之后，状态为 ：" + trace.getState());
        System.out.println("trace 用时 : " + trace.getDuration() + "ms");

        System.out.println("经过存储策略筛选过后，剩下的 ： " + StorageUtil.getStorage().getTracing().size());
    }

    public void consumerSOA(String traceId, String branchId) {

    }
}
