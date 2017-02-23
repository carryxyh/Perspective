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
        Ender ender = new Ender("weixin-meal 中的一个方法 over", trace.getTraceId(), mainWId);
//        ender.setError(new NullPointerException("空指针异常"));
        trace.endOneBranch(mainWId, ender);

        /*--------------------------到这里第一层结束----------------------------*/
        trace.endOneBranch(mainWId, new Ender("weixin-meal over", trace.getTraceId(), mainT.getBranchId()));

        System.out.println("trace 追踪之后，状态为 ：" + trace.getState());
        System.out.println("trace 用时 : " + trace.getDuration() + "ms");

        if (trace.getErrorBranch().size() > 0) {
            for (Branch branch : trace.getErrorBranch()) {
                System.out.println("有问题的 branch 为 : " + branch.getName() + " 存在的异常为 : " + branch.getError());
            }
        } else {
            System.out.println("整个链路没有问题！");
        }

        System.out.println("经过存储策略筛选过后，剩下的 ： " + StorageUtil.getStorage().getTracing().size());
    }

    public void testMoreInfo() throws Exception {
        LocalTraceManager localTraceManager = LocalTraceManager.getInstance();
        Trace trace = new Trace("weixin - meal trace", SymbolFactory.createTraceId());
        localTraceManager.add(trace);

        /*-------------------到这里第一层开始-----------------------------*/

        //weixin-meal main
        Branch mainT = trace.getMainBranch();
        Branch consumerB = new Branch("consumer - soa", localTraceManager.get().getTraceId(), localTraceManager.get().getTraceId() + "-2", mainT);
        mainT.addInvoke(consumerB);

        /*-------------------到这里第二层开始-----------------------------*/

        //模拟方法调用耗时
        Thread.sleep(100);

        Ender enderConsumer = new Ender("consumer - soa over", trace.getTraceId(), consumerB.getBranchId());
//        enderConsumer.setError(new NullPointerException("空指针！"));
        trace.endOneBranch(consumerB.getBranchId(), enderConsumer);

        /*--------------------------到这里第一层结束----------------------------*/

        Ender enderWX = new Ender("weixin - meal over", trace.getTraceId(), mainT.getBranchId());
//        enderWX.setError(new IllegalStateException("状态异常！"));
        trace.endOneBranch(mainT.getBranchId(), enderWX);

        System.out.println("trace 追踪之后，状态为 ：" + trace.getState());
        System.out.println("trace 用时 : " + trace.getDuration() + "ms");

        if (trace.getErrorBranch().size() > 0) {
            System.out.println(trace.format());
        } else {
            System.out.println("整个链路没有问题！");
        }

        System.out.println("经过存储策略筛选过后，剩下的 ： " + StorageUtil.getStorage().getTracing().size());
    }

    public void consumerSOA(String traceId, String branchId) {

    }
}
