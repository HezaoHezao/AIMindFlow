package org.aimindflow.common.ratelimiter.enums;

/**
 * 限流类型
 *
 * @author HezaoHezao
 */
public enum LimitType {
    /**
     * 默认策略全局限流
     */
    DEFAULT,

    /**
     * 根据请求者IP进行限流
     */
    IP,

    /**
     * 根据用户ID进行限流
     */
    USER
}