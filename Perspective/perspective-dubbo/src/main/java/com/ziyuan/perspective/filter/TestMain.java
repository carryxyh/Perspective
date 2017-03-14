package com.ziyuan.perspective.filter;

import com.alibaba.dubbo.rpc.Filter;

/**
 * TestMain
 *
 * @author ziyuan
 * @since 2017-03-14
 */
public class TestMain {
    public static void main(String[] args) {
        Class c = Filter.class;
        System.out.println(c.isAssignableFrom(PerspectiveFilter.class));
    }
}
