package org.aimindflow.common.ratelimiter.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.ratelimiter.RateLimiter;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 基于Redis的限流器实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisRateLimiter implements RateLimiter {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<Long> limitScript;

    @Override
    public boolean tryAcquire(String key, int count, int time) {
        try {
            // 执行Lua脚本
            Long result = stringRedisTemplate.execute(
                    limitScript,
                    Collections.singletonList(key),
                    String.valueOf(count),
                    String.valueOf(time)
            );

            // 判断是否获取到令牌
            return result != null && result == 1L;
        } catch (Exception e) {
            log.error("Redis限流器异常", e);
            // 当Redis发生异常时，为了系统的可用性，允许请求通过
            return true;
        }
    }
}