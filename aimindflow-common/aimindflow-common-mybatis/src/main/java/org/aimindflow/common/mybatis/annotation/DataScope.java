package org.aimindflow.common.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 *
 * @author HezaoHezao
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 部门表的别名
     */
    String deptAlias() default "";

    /**
     * 用户表的别名
     */
    String userAlias() default "";

    /**
     * 是否仅查询本人数据
     */
    boolean self() default false;

    /**
     * 是否启用数据权限
     */
    boolean enabled() default true;
}