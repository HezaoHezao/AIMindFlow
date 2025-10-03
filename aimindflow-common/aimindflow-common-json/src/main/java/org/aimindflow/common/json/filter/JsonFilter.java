package org.aimindflow.common.json.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * JSON过滤器注解
 * 用于在序列化过程中过滤特定字段
 *
 * @author HezaoHezao
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonFilter {

    /**
     * 包含的字段名称
     * 如果为空，则不进行包含过滤
     *
     * @return 包含的字段名称数组
     */
    String[] includes() default {};

    /**
     * 排除的字段名称
     * 如果为空，则不进行排除过滤
     *
     * @return 排除的字段名称数组
     */
    String[] excludes() default {};
}