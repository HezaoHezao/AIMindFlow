package org.aimindflow.common.redis.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Redis分布式锁
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisDistributedLock {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LOCK_PREFIX = "lock:";
    private static final String UNLOCK_SCRIPT = 
        "if redis.call('get', KEYS[1]) == ARGV[1] then " +
        "return redis.call('del', KEYS[1]) " +
        "else " +
        "return 0 " +
        "end";

    /**
     * 尝试获取锁
     *
     * @param key        锁键
     * @param expireTime 过期时间（秒）
     * @return 锁标识，获取失败返回null
     */
    public String tryLock(String key, long expireTime) {
        return tryLock(key, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 尝试获取锁
     *
     * @param key        锁键
     * @param expireTime 过期时间
     * @param timeUnit   时间单位
     * @return 锁标识，获取失败返回null
     */
    public String tryLock(String key, long expireTime, TimeUnit timeUnit) {
        try {
            String lockKey = LOCK_PREFIX + key;
            String lockValue = UUID.randomUUID().toString();
            
            Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, expireTime, timeUnit);
            
            if (Boolean.TRUE.equals(success)) {
                log.debug("获取分布式锁成功，key: {}, value: {}", lockKey, lockValue);
                return lockValue;
            }
            
            log.debug("获取分布式锁失败，key: {}", lockKey);
            return null;
        } catch (Exception e) {
            log.error("获取分布式锁异常，key: {}", key, e);
            return null;
        }
    }

    /**
     * 尝试获取锁（带重试）
     *
     * @param key        锁键
     * @param expireTime 过期时间（秒）
     * @param retryTimes 重试次数
     * @param sleepTime  重试间隔（毫秒）
     * @return 锁标识，获取失败返回null
     */
    public String tryLockWithRetry(String key, long expireTime, int retryTimes, long sleepTime) {
        for (int i = 0; i <= retryTimes; i++) {
            String lockValue = tryLock(key, expireTime);
            if (lockValue != null) {
                return lockValue;
            }
            
            if (i < retryTimes) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.warn("获取分布式锁重试被中断，key: {}", key);
                    return null;
                }
            }
        }
        
        log.warn("获取分布式锁重试失败，key: {}, retryTimes: {}", key, retryTimes);
        return null;
    }

    /**
     * 释放锁
     *
     * @param key       锁键
     * @param lockValue 锁标识
     * @return 是否释放成功
     */
    public boolean unlock(String key, String lockValue) {
        try {
            String lockKey = LOCK_PREFIX + key;
            DefaultRedisScript<Long> script = new DefaultRedisScript<>(UNLOCK_SCRIPT, Long.class);
            Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), lockValue);
            
            boolean success = Long.valueOf(1).equals(result);
            if (success) {
                log.debug("释放分布式锁成功，key: {}, value: {}", lockKey, lockValue);
            } else {
                log.warn("释放分布式锁失败，锁可能已过期或被其他线程释放，key: {}, value: {}", lockKey, lockValue);
            }
            
            return success;
        } catch (Exception e) {
            log.error("释放分布式锁异常，key: {}, value: {}", key, lockValue, e);
            return false;
        }
    }

    /**
     * 强制释放锁（不检查锁标识）
     *
     * @param key 锁键
     * @return 是否释放成功
     */
    public boolean forceUnlock(String key) {
        try {
            String lockKey = LOCK_PREFIX + key;
            Boolean result = redisTemplate.delete(lockKey);
            
            boolean success = Boolean.TRUE.equals(result);
            if (success) {
                log.debug("强制释放分布式锁成功，key: {}", lockKey);
            } else {
                log.warn("强制释放分布式锁失败，锁可能不存在，key: {}", lockKey);
            }
            
            return success;
        } catch (Exception e) {
            log.error("强制释放分布式锁异常，key: {}", key, e);
            return false;
        }
    }

    /**
     * 检查锁是否存在
     *
     * @param key 锁键
     * @return 是否存在
     */
    public boolean isLocked(String key) {
        try {
            String lockKey = LOCK_PREFIX + key;
            return Boolean.TRUE.equals(redisTemplate.hasKey(lockKey));
        } catch (Exception e) {
            log.error("检查分布式锁状态异常，key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取锁的剩余过期时间
     *
     * @param key 锁键
     * @return 剩余过期时间（秒），-1表示永不过期，-2表示锁不存在
     */
    public long getLockExpire(String key) {
        try {
            String lockKey = LOCK_PREFIX + key;
            return redisTemplate.getExpire(lockKey, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("获取分布式锁过期时间异常，key: {}", key, e);
            return -2;
        }
    }
}