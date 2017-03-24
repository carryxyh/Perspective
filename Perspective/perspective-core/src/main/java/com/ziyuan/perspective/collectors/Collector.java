
package com.ziyuan.perspective.collectors;

import com.ziyuan.perspective.invokes.Trace;

import java.io.Serializable;
import java.util.List;

/**
 * Collector 收集信息的组件
 *
 * @author ziyuan
 * @since 2017-02-20
 */
public interface Collector extends Serializable {

    /**
     * 去storage中取
     *
     * @return
     */
    List<Trace> fetch();

    /**
     * storage 主动送过来
     *
     * @param formatables
     */
    void receive(List<Trace> formatables);

    /**
     * 开始手机
     */
    void beginCollect();

    /**
     * 关闭收集
     */
    void shutDownNow();
}
