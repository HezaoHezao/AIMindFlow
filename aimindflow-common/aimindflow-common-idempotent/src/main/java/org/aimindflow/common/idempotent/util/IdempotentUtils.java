package org.aimindflow.common.idempotent.util;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.idempotent.constant.IdempotentConstants;
import org.aimindflow.common.idempotent.exception.IdempotentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 幂等工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class IdempotentUtils {

    /**
     * Redis模板
     */
    private static StringRedisTemplate redisTemplate;

    /**
     * 释放锁的Lua脚本
     */
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        IdempotentUtils.redisTemplate = redisTemplate;
    }

    /**
     * 执行幂等操作
     *
     * @param key        幂等Key
     * @param expireTime 过期时间（秒）
     * @param supplier   操作
     * @param <T>        返回值类型
     * @return 操作结果
     */
    public static <T> T execute(String key, int expireTime, Supplier<T> supplier) {
        return execute(key, expireTime, IdempotentConstants.DEFAULT_MESSAGE, supplier);
    }

    /**
     * 执行幂等操作
     *
     * @param key        幂等Key
     * @param expireTime 过期时间（秒）
     * @param message    提示消息
     * @param supplier   操作
     * @param <T>        返回值类型
     * @return 操作结果
     */
    public static <T> T execute(String key, int expireTime, String message, Supplier<T> supplier) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate未初始化");
        }

        if (!StringUtils.hasText(key)) {
            throw new IdempotentException(IdempotentConstants.ERROR_GENERATE_KEY);
        }

        // 添加前缀
        if (!key.startsWith(IdempotentConstants.REDIS_KEY_PREFIX)) {
            key = IdempotentConstants.REDIS_KEY_PREFIX + key;
        }

        // 获取锁
        String value = UUID.randomUUID().toString();
        boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
        if (!acquired) {
            log.warn("幂等性校验失败，请勿重复提交");
            throw new IdempotentException(message);
        }

        try {
            // 执行操作
            return supplier.get();
        } finally {
            // 释放锁
            releaseLock(key, value);
        }
    }

    /**
     * 检查是否可以执行
     *
     * @param key        幂等Key
     * @param expireTime 过期时间（秒）
     * @return 是否可以执行
     */
    public static boolean checkExecutable(String key, int expireTime) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate未初始化");
        }

        if (!StringUtils.hasText(key)) {
            return false;
        }

        // 添加前缀
        if (!key.startsWith(IdempotentConstants.REDIS_KEY_PREFIX)) {
            key = IdempotentConstants.REDIS_KEY_PREFIX + key;
        }

        // 检查是否存在
        Boolean exists = redisTemplate.hasKey(key);
        return exists == null || !exists;
    }

    /**
     * 设置幂等标记
     *
     * @param key        幂等Key
     * @param expireTime 过期时间（秒）
     * @return 是否设置成功
     */
    public static boolean setIdempotentMark(String key, int expireTime) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate未初始化");
        }

        if (!StringUtils.hasText(key)) {
            return false;
        }

        // 添加前缀
        if (!key.startsWith(IdempotentConstants.REDIS_KEY_PREFIX)) {
            key = IdempotentConstants.REDIS_KEY_PREFIX + key;
        }

        // 设置标记
        String value = UUID.randomUUID().toString();
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS));
    }

    /**
     * 删除幂等标记
     *
     * @param key 幂等Key
     * @return 是否删除成功
     */
    public static boolean removeIdempotentMark(String key) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate未初始化");
        }

        if (!StringUtils.hasText(key)) {
            return false;
        }

        // 添加前缀
        if (!key.startsWith(IdempotentConstants.REDIS_KEY_PREFIX)) {
            key = IdempotentConstants.REDIS_KEY_PREFIX + key;
        }

        // 删除标记
        Boolean deleted = redisTemplate.delete(key);
        return deleted != null && deleted;
    }

    /**
     * 释放锁
     *
     * @param key   锁的Key
     * @param value 锁的值
     */
    private static void releaseLock(String key, String value) {
        try {
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(RELEASE_LOCK_SCRIPT, Long.class);
            Long result = redisTemplate.execute(script, Collections.singletonList(key), value);
            if (result == null || result == 0) {
                log.warn("释放锁失败，可能已过期或被其他线程释放");
            }
        } catch (Exception e) {
            log.error("释放锁异常", e);
        }
    }
}