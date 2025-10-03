package org.aimindflow.common.redis.manager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.redis.config.RedisProperties;
import org.aimindflow.common.redis.utils.RedisUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis缓存管理器
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisCacheManager {

    private final RedisUtils redisUtils;
    private final RedisProperties redisProperties;

    /**
     * 获取缓存，如果不存在则执行supplier并缓存结果
     *
     * @param key      缓存键
     * @param supplier 数据提供者
     * @param <T>      数据类型
     * @return 缓存数据
     */
    public <T> T getOrSet(String key, Supplier<T> supplier) {
        return getOrSet(key, supplier, redisProperties.getDefaultExpireTime(), TimeUnit.SECONDS);
    }

    /**
     * 获取缓存，如果不存在则执行supplier并缓存结果
     *
     * @param key      缓存键
     * @param supplier 数据提供者
     * @param timeout  过期时间
     * @param unit     时间单位
     * @param <T>      数据类型
     * @return 缓存数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getOrSet(String key, Supplier<T> supplier, long timeout, TimeUnit unit) {
        try {
            String cacheKey = buildCacheKey(key);
            Object cached = redisUtils.get(cacheKey);
            if (cached != null) {
                return (T) cached;
            }

            T data = supplier.get();
            if (data != null) {
                redisUtils.set(cacheKey, data, timeout, unit);
            }
            return data;
        } catch (Exception e) {
            log.error("Redis缓存操作失败，key: {}", key, e);
            return supplier.get();
        }
    }

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    public void set(String key, Object value) {
        set(key, value, redisProperties.getDefaultExpireTime(), TimeUnit.SECONDS);
    }

    /**
     * 设置缓存
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void set(String key, Object value, long timeout, TimeUnit unit) {
        try {
            String cacheKey = buildCacheKey(key);
            redisUtils.set(cacheKey, value, timeout, unit);
        } catch (Exception e) {
            log.error("Redis缓存设置失败，key: {}", key, e);
        }
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @param <T> 数据类型
     * @return 缓存数据
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        try {
            String cacheKey = buildCacheKey(key);
            return (T) redisUtils.get(cacheKey);
        } catch (Exception e) {
            log.error("Redis缓存获取失败，key: {}", key, e);
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    public void delete(String key) {
        try {
            String cacheKey = buildCacheKey(key);
            redisUtils.del(cacheKey);
        } catch (Exception e) {
            log.error("Redis缓存删除失败，key: {}", key, e);
        }
    }

    /**
     * 删除缓存（支持通配符）
     *
     * @param pattern 缓存键模式
     */
    public void deleteByPattern(String pattern) {
        try {
            String cachePattern = buildCacheKey(pattern);
            redisUtils.delByPattern(cachePattern);
        } catch (Exception e) {
            log.error("Redis缓存批量删除失败，pattern: {}", pattern, e);
        }
    }

    /**
     * 判断缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    public boolean exists(String key) {
        try {
            String cacheKey = buildCacheKey(key);
            return redisUtils.hasKey(cacheKey);
        } catch (Exception e) {
            log.error("Redis缓存存在性检查失败，key: {}", key, e);
            return false;
        }
    }

    /**
     * 设置缓存过期时间
     *
     * @param key     缓存键
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        try {
            String cacheKey = buildCacheKey(key);
            redisUtils.expire(cacheKey, timeout, unit);
        } catch (Exception e) {
            log.error("Redis缓存过期时间设置失败，key: {}", key, e);
        }
    }

    /**
     * 获取缓存剩余过期时间
     *
     * @param key 缓存键
     * @return 剩余过期时间（秒）
     */
    public long getExpire(String key) {
        try {
            String cacheKey = buildCacheKey(key);
            return redisUtils.getExpire(cacheKey);
        } catch (Exception e) {
            log.error("Redis缓存过期时间获取失败，key: {}", key, e);
            return -1;
        }
    }

    /**
     * 构建缓存键
     *
     * @param key 原始键
     * @return 带前缀的缓存键
     */
    private String buildCacheKey(String key) {
        String prefix = redisProperties.getCachePrefix();
        if (prefix != null && !prefix.isEmpty()) {
            return prefix + ":" + key;
        }
        return key;
    }
}