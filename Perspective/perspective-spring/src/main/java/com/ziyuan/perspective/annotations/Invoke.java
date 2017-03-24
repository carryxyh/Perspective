
package com.ziyuan.perspective.annotations;

import com.ziyuan.perspective.InvokeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Invoke 生成invoke的切面
 *
 * @author ziyuan
 * @since 2017-03-06
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Invoke {

    /**
     * invokeNode的名称
     *
     * @return invokeNode的名称
     */
    String name() default "";

    /**
     * owner branch的id
     *
     * @return branchId
     */
    String ownerBranchId() default "";

    /**
     * 中央trace的id
     *
     * @return traceId
     */
    String traceId() default "";

    /**
     * 那种invoke，node或者branch
     *
     * @return 一个方法的invoke种类
     */
    InvokeType type() default InvokeType.NODE;
}
