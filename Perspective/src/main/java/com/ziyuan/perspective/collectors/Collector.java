/*
 * Copyright (C) 2009-2016 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package com.ziyuan.perspective.collectors;

import com.ziyuan.perspective.Formatable;

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
    List<Formatable> fetch();

    /**
     * storage 主动送过来
     *
     * @param formatables
     */
    void receive(List<Formatable> formatables);
}
