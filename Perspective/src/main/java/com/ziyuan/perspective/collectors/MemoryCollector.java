/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.collectors;

import com.ziyuan.perspective.Formatable;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.storages.Storage;
import com.ziyuan.perspective.util.StorageUtil;
import com.ziyuan.perspective.util.StrategyUtil;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * MemoryCollector
 *
 * @author ziyuan
 * @since 2017-02-23
 */
public final class MemoryCollector implements Collector {

    /**
     * storage的引用
     */
    private final Storage storage = StorageUtil.getStorage();

    private final ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(5, new ThreadFactory() {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Collector Thread");
            return t;
        }
    });

    /**
     * 3分钟收集一次
     */
    public void beginCollect() {
        scheduled.scheduleAtFixedRate(new Runnable() {
            public void run() {
                List<Trace> result = fetch();
                if (CollectionUtils.isEmpty(result)) {
                    return;
                }
                List<Formatable> finalResult = new ArrayList<Formatable>();
                for (Trace trace : result) {
                    if (StrategyUtil.filterCollect(trace)) {
                        finalResult.add(trace);
                    }
                }
            }
        }, 1, 180, TimeUnit.SECONDS);
    }

    public List<Trace> fetch() {
        return null;
    }

    public void receive(List<Trace> formatables) {

    }

    public void shutDownNow() {
        scheduled.shutdown();
    }

    /**
     * 处理结果
     *
     * @param result 结果
     */
    private void outPutResult(List<Formatable> result) {

    }
}
