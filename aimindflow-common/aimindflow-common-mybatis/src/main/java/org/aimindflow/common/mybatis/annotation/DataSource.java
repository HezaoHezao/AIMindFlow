package org.aimindflow.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 多数据源切换注解
 *
 * @author HezaoHezao
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    /**
     * 数据源名称
     */
    String value() default "";
}