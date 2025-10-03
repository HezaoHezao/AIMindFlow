package org.aimindflow.common.ratelimiter.annotation;

import org.aimindflow.common.ratelimiter.enums.LimitType;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author HezaoHezao
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

    /**
     * 限流key
     */
    String key() default "";

    /**
     * 限流时间,单位秒
     */
    int time() default 60;

    /**
     * 限流次数
     */
    int count() default 100;

    /**
     * 限流类型
     */
    LimitType limitType() default LimitType.DEFAULT;

    /**
     * 限流提示消息
     */
    String message() default "访问过于频繁，请稍后再试";
}