
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * CollectStrategy 收集策略
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public interface CollectStrategy extends Strategy {

    /**
     * 手机trace的策略
     *
     * @param trace trace
     * @return 是否收集
     */
    boolean collectTrace(Trace trace);
}
