package org.aimindflow.common.ratelimiter.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.ratelimiter.RateLimiter;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 基于Guava的限流器实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class GuavaRateLimiter implements RateLimiter {

    /**
     * 限流器缓存
     */
    private final Cache<String, com.google.common.util.concurrent.RateLimiter> rateLimiterCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(1, TimeUnit.HOURS)
            .build();

    @Override
    public boolean tryAcquire(String key, int count, int time) {
        try {
            // 获取限流器，如果不存在则创建
            com.google.common.util.concurrent.RateLimiter rateLimiter = rateLimiterCache.get(key, () -> {
                // 计算每秒允许的请求数
                double permitsPerSecond = (double) count / time;
                // 创建限流器
                return com.google.common.util.concurrent.RateLimiter.create(permitsPerSecond);
            });

            // 尝试获取令牌
            return rateLimiter.tryAcquire(1, 0, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Guava限流器异常", e);
            // 当发生异常时，为了系统的可用性，允许请求通过
            return true;
        }
    }
}