
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * ErrorStorageStrategy
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public final class ErrorStorageStrategy implements StorageStrategy {

    public boolean storeTrace(Trace trace) {
        //trace成功结束，返回false 不存
        return !trace.isSuccess();
    }
}
