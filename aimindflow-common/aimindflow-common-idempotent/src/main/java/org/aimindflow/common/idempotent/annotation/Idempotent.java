package org.aimindflow.common.idempotent.annotation;

import org.aimindflow.common.idempotent.constant.IdempotentConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 幂等注解，用于标记需要进行幂等控制的方法
 *
 * @author HezaoHezao
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * 幂等Key的前缀，用于区分不同业务
     * 默认为方法名
     */
    String prefix() default "";

    /**
     * 幂等Key的生成策略
     * 默认为参数策略，即根据方法参数生成幂等Key
     */
    IdempotentStrategy strategy() default IdempotentStrategy.PARAMS;

    /**
     * 幂等Key的过期时间，单位：秒
     * 默认为5分钟
     */
    int expireTime() default IdempotentConstants.DEFAULT_EXPIRE_TIME;

    /**
     * 提示消息
     */
    String message() default IdempotentConstants.DEFAULT_MESSAGE;

    /**
     * 是否在方法执行完成后删除幂等Key
     * 默认为true，即方法执行完成后删除幂等Key
     * 如果设置为false，则幂等Key会在过期时间后自动删除
     */
    boolean deleteKeyAfterExecution() default true;

    /**
     * 幂等Key的生成策略枚举
     */
    enum IdempotentStrategy {
        /**
         * 参数策略，根据方法参数生成幂等Key
         */
        PARAMS,

        /**
         * 自定义策略，需要实现IdempotentKeyGenerator接口
         */
        CUSTOM,

        /**
         * SpEL表达式策略，根据SpEL表达式生成幂等Key
         */
        SPEL
    }
}