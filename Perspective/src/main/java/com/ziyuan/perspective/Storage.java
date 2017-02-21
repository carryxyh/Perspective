/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective;

/**
 * Storage 存储中心,必须单例
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public interface Storage {

    Trace findTraceById(String traceId);

    Branch findBranch(String traceId, String branchId);
}
