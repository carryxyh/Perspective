/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.aspect;

import com.ziyuan.perspective.InvokeType;
import com.ziyuan.perspective.annotations.Invoke;
import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Ender;
import com.ziyuan.perspective.invokes.InvokeNode;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * InvokeAspect 用于自动的生成invokeNode
 *
 * @author ziyuan
 * @since 2017-02-28
 */
@Component
@Aspect
public class InvokeAspect {

    private Storage storage = StorageUtil.getStorage();

    @Around("execution(com.ziyuan.perspective.annotations.Invoke)")
    public Object invokeInit(ProceedingJoinPoint pjp) throws Throwable {

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Invoke invoke = method.getAnnotation(Invoke.class);

        Trace centreTrace = storage.findTraceById(invoke.traceId());
        Branch owner = storage.findBranch(centreTrace.getTraceId(), invoke.ownerBranchId());
        Branch b;
        if (invoke.type() == InvokeType.BRANCH) {
            //生成一个branch
            b = new Branch(invoke.name(), invoke.traceId(), owner);
            owner.addInvoke(b);
        } else {
            //生成一个node
            InvokeNode invokenode = new InvokeNode(invoke.name(), centreTrace.getTraceId(), invoke.ownerBranchId());
            owner.addInvoke(invokenode);
            b = invokenode.getMainBranch();
        }
        Ender ender = new Ender(b.getName() + "- end", centreTrace.getTraceId(), b.getBranchId());
        try {
            return pjp.proceed();
        } catch (Throwable throwable) {
            ender.setError(throwable);
            throw throwable;
        } finally {
            centreTrace.endOneBranch(b.getBranchId(), ender);
        }
    }
}
