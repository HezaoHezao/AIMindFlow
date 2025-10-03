package org.aimindflow.common.redis.config;

import org.aimindflow.common.redis.lock.RedisDistributedLock;
import org.aimindflow.common.redis.manager.RedisCacheManager;
import org.aimindflow.common.redis.utils.RedisUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.*;

/**
 * Redis自动配置类
 *
 * @author HezaoHezao
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.redis", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(RedisConfig.class)
public class RedisAutoConfiguration {

    /**
     * Redis工具类
     *
     * @param redisTemplate     Redis模板
     * @param valueOperations   字符串操作模板
     * @param hashOperations    哈希操作模板
     * @param listOperations    列表操作模板
     * @param setOperations     集合操作模板
     * @param zSetOperations    有序集合操作模板
     * @param redisProperties   Redis属性配置
     * @return Redis工具类
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RedisTemplate.class, ValueOperations.class, HashOperations.class, 
                       ListOperations.class, SetOperations.class, ZSetOperations.class})
    public RedisUtils redisUtils(RedisTemplate<String, Object> redisTemplate,
                                ValueOperations<String, Object> valueOperations,
                                HashOperations<String, String, Object> hashOperations,
                                ListOperations<String, Object> listOperations,
                                SetOperations<String, Object> setOperations,
                                ZSetOperations<String, Object> zSetOperations,
                                RedisProperties redisProperties) {
        return new RedisUtils(redisTemplate, valueOperations, hashOperations, 
                             listOperations, setOperations, zSetOperations, redisProperties);
    }

    /**
     * Redis缓存管理器
     *
     * @param redisUtils      Redis工具类
     * @param redisProperties Redis属性配置
     * @return Redis缓存管理器
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisUtils.class)
    public RedisCacheManager redisCacheManager(RedisUtils redisUtils, RedisProperties redisProperties) {
        return new RedisCacheManager(redisUtils, redisProperties);
    }

    /**
     * Redis分布式锁
     *
     * @param redisTemplate Redis模板
     * @return Redis分布式锁
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisDistributedLock redisDistributedLock(RedisTemplate<String, Object> redisTemplate) {
        return new RedisDistributedLock(redisTemplate);
    }
}