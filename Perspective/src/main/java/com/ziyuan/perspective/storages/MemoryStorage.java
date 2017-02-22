/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.storages;

import com.ziyuan.perspective.invokes.Branch;
import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.util.StrategyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * MemoryStorage 内存存储
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public class MemoryStorage implements Storage {

    /**
     * 所有Trace的集合，包括正在进行的和结束的
     */
    private final Map<String, Trace> tracing = new ConcurrentHashMap<String, Trace>(128);

    /**
     * 所有结束的trace集合
     */
    private final List<Trace> overTraces = new CopyOnWriteArrayList<Trace>();

    public Trace findTraceById(String traceId) {
        return tracing.get(traceId);
    }

    public Branch findBranch(String traceId, String branchId) {
        Trace t = tracing.get(traceId);
        if (t == null) {
            return null;
        }
        return t.getOneBranch(branchId);
    }

    /**
     * 获得结束的trace集合
     *
     * @return 结束的trace集合
     */
    public List<Trace> getOverTraces() {
        return overTraces;
    }

    public void endOneTrace(Trace trace) {
        if (trace == null) {
            return;
        }
        tracing.remove(trace.getTraceId());
        //这里需要用到策略，来控制什么样的trace需要放入到overTraces中
        if (StrategyUtil.filterStorage(trace)) {
            overTraces.add(trace);
        }
    }

    public ArrayList<Trace> getTracing() {
        return new ArrayList<Trace>(tracing.values());
    }

    public void clear() {
        tracing.clear();
        overTraces.clear();
    }

    /**
     * 单例
     */
    private static final MemoryStorage instance = new MemoryStorage();

    private MemoryStorage() {
        //添加钩子，虚拟机结束忘记调用clear也会自动清理
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                clear();
            }
        }));
    }

    public static MemoryStorage getInstance() {
        return instance;
    }
}
