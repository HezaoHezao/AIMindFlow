package org.aimindflow.common.sensitive.annotation;

import org.aimindflow.common.sensitive.enums.SensitiveType;

import java.lang.annotation.*;

/**
 * 敏感数据注解
 * 用于标记需要脱敏处理的字段
 *
 * @author HezaoHezao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sensitive {

    /**
     * 敏感数据类型
     */
    SensitiveType type();

    /**
     * 自定义脱敏规则（当type为CUSTOM时使用）
     * 格式：startIndex,endIndex,maskChar
     * 例如：3,4,* 表示保留前3位和后4位，中间用*替换
     */
    String rule() default "";

    /**
     * 是否启用脱敏（可用于动态控制）
     */
    boolean enabled() default true;
}