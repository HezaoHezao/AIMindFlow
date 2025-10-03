package org.aimindflow.common.log.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志注解
 *
 * @author HezaoHezao
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 模块名称
     */
    String module() default "";

    /**
     * 操作类型
     */
    String operationType() default "";

    /**
     * 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     */
    boolean recordParams() default true;

    /**
     * 是否记录响应结果
     */
    boolean recordResult() default true;

    /**
     * 是否记录异常信息
     */
    boolean recordException() default true;

    /**
     * 是否记录执行时间
     */
    boolean recordExecutionTime() default true;

    /**
     * 是否记录操作人信息
     */
    boolean recordOperator() default true;
}