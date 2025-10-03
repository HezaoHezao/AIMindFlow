package org.aimindflow.common.ratelimiter.config;

import org.aimindflow.common.ratelimiter.aspect.RateLimitAspect;
import org.aimindflow.common.ratelimiter.impl.GuavaRateLimiter;
import org.aimindflow.common.ratelimiter.impl.RedisRateLimiter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

/**
 * 限流自动配置类
 *
 * @author HezaoHezao
 */
@Configuration
@Import({RedisLimitScript.class})
public class RateLimiterAutoConfiguration {

    /**
     * Redis限流器配置
     */
    @Configuration
    @ConditionalOnClass(StringRedisTemplate.class)
    @ConditionalOnProperty(prefix = "aimindflow.rate-limit", name = "type", havingValue = "redis", matchIfMissing = true)
    public static class RedisLimiterConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public RedisRateLimiter redisRateLimiter(StringRedisTemplate stringRedisTemplate, RedisScript<Long> limitScript) {
            return new RedisRateLimiter(stringRedisTemplate, limitScript);
        }
    }

    /**
     * Guava限流器配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "aimindflow.rate-limit", name = "type", havingValue = "guava")
    public static class GuavaLimiterConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public GuavaRateLimiter guavaRateLimiter() {
            return new GuavaRateLimiter();
        }
    }

    /**
     * 限流切面配置
     */
    @Configuration
    @ConditionalOnProperty(prefix = "aimindflow.rate-limit", name = "enabled", havingValue = "true", matchIfMissing = true)
    public static class RateLimitAspectConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean({RedisRateLimiter.class, GuavaRateLimiter.class})
        public RateLimitAspect rateLimitAspect() {
            return new RateLimitAspect();
        }
    }
}