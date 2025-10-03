package org.aimindflow.common.ratelimiter;

/**
 * 限流器接口
 *
 * @author HezaoHezao
 */
public interface RateLimiter {

    /**
     * 尝试获取令牌
     *
     * @param key   限流key
     * @param count 限流次数
     * @param time  限流时间窗口（秒）
     * @return 是否获取成功
     */
    boolean tryAcquire(String key, int count, int time);
}