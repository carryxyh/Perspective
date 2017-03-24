
package com.ziyuan.perspective.util;

import com.ziyuan.perspective.invokes.Trace;
import com.ziyuan.perspective.strategy.CollectStrategy;
import com.ziyuan.perspective.strategy.ErrorCollectStrategy;
import com.ziyuan.perspective.strategy.ErrorStorageStrategy;
import com.ziyuan.perspective.strategy.StorageStrategy;

/**
 * StrategyUtil 主要用来把storage、collector和strategy解耦
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public final class StrategyUtil {

    /**
     * 存储策略
     */
    public static StorageStrategy storageStrategy = new ErrorStorageStrategy();

    /**
     * 收集策略
     */
    public static CollectStrategy collectStrategy = new ErrorCollectStrategy();

    /**
     * 过滤一个需要存储的trace
     *
     * @param trace trace
     * @return 是否需要存储
     */
    public static boolean filterStorage(Trace trace) {
        return storageStrategy.storeTrace(trace);
    }

    /**
     * 过滤一个需要收集的trace
     *
     * @param trace trace
     * @return 是否需要收集
     */
    public static boolean filterCollect(Trace trace) {
        return collectStrategy.collectTrace(trace);
    }
}
