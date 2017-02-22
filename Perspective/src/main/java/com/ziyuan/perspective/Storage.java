/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Trace;

/**
 * Storage 存储中心,必须单例
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public interface Storage {

    /**
     * 通过traceId获取一个trace
     *
     * @param traceId traceId
     * @return trace
     */
    Trace findTraceById(String traceId);

    /**
     * 通过traceId和branchId获取一个trace中的某个branch
     *
     * @param traceId  traceId
     * @param branchId branchId
     * @return branch
     */
    Branch findBranch(String traceId, String branchId);

    /**
     * 结束一个trace
     *
     * @param trace 结束了要被处理的trace
     */
    void endOneTrace(Trace trace);
}
