package org.aimindflow.common.idempotent.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.idempotent.annotation.Idempotent;
import org.aimindflow.common.idempotent.exception.IdempotentException;
import org.aimindflow.common.idempotent.generator.IdempotentKeyGenerator;
import org.aimindflow.common.idempotent.generator.IdempotentKeyGeneratorFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 幂等切面
 *
 * @author HezaoHezao
 */
@Slf4j
@Aspect
@Component
public class IdempotentAspect {

    /**
     * Redis模板
     */
    private final StringRedisTemplate redisTemplate;

    /**
     * Key生成器工厂
     */
    private final IdempotentKeyGeneratorFactory keyGeneratorFactory;

    /**
     * 释放锁的Lua脚本
     */
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    public IdempotentAspect(StringRedisTemplate redisTemplate, IdempotentKeyGeneratorFactory keyGeneratorFactory) {
        this.redisTemplate = redisTemplate;
        this.keyGeneratorFactory = keyGeneratorFactory;
    }

    /**
     * 定义切点
     */
    @Pointcut("@annotation(org.aimindflow.common.idempotent.annotation.Idempotent)")
    public void idempotentPointcut() {
    }

    /**
     * 环绕通知
     *
     * @param point 切点
     * @return 方法返回值
     * @throws Throwable 异常
     */
    @Around("idempotentPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取Idempotent注解
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        if (idempotent == null) {
            return point.proceed();
        }

        // 获取幂等Key
        String key = generateKey(idempotent, point);
        if (!StringUtils.hasText(key)) {
            log.error("生成幂等Key失败");
            throw new IdempotentException("生成幂等Key失败");
        }

        // 获取锁
        String value = String.valueOf(System.currentTimeMillis());
        boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, value, idempotent.expireTime(), TimeUnit.SECONDS);
        if (!acquired) {
            log.warn("幂等性校验失败，请勿重复提交");
            throw new IdempotentException(idempotent.message());
        }

        try {
            // 执行方法
            return point.proceed();
        } finally {
            // 如果需要在方法执行完成后删除幂等Key
            if (idempotent.deleteKeyAfterExecution()) {
                releaseLock(key, value);
            }
        }
    }

    /**
     * 生成幂等Key
     *
     * @param idempotent 幂等注解
     * @param point      切点
     * @return 幂等Key
     */
    private String generateKey(Idempotent idempotent, ProceedingJoinPoint point) {
        // 获取Key生成器
        IdempotentKeyGenerator keyGenerator = keyGeneratorFactory.getKeyGenerator(idempotent.strategy());
        return keyGenerator.generateKey(idempotent.prefix(), point);
    }

    /**
     * 释放锁
     *
     * @param key   锁的Key
     * @param value 锁的值
     */
    private void releaseLock(String key, String value) {
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