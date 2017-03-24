
package com.ziyuan.perspective.strategy;

import com.ziyuan.perspective.invokes.Trace;

/**
 * AllStorageStrategy
 *
 * @author ziyuan
 * @since 2017-02-22
 */
public final class AllStorageStrategy implements StorageStrategy {

    public boolean storeTrace(Trace trace) {
        //不管什么样的trace都存
        return true;
    }
}
