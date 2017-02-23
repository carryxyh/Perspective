/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.checker;

import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * ScheduleChecker 这个checker用来检查超时的Trace
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public final class ScheduleChecker implements Checker {

    /**
     * storage结构
     */
    private final Storage storage = StorageUtil.getStorage();

    private final ExecutorService executorService = Executors.newFixedThreadPool(10, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Check worker Thread");
            return t;
        }
    });

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(2, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "ScheduleChecker Thread");
            return t;
        }
    });

    /**
     * 检查
     */
    public void checkBegin() {
        final long now = System.currentTimeMillis();
        final List<Trace> tracing = storage.getTracing();
        //TODO 这里需要分片去检查比较好
        executorService.submit(new Runnable() {
            public void run() {
                for (Trace trace : tracing) {
                    if (trace.checkTimeOut(now)) {
                        storage.remove(trace.getTraceId());
                    }
                }
            }
        });
    }

    /**
     * 结束检查
     */
    public void shutDown() {
        scheduled.shutdown();
        executorService.shutdown();
    }
}
