package org.aimindflow.common.idempotent.generator;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 幂等Key生成器接口
 *
 * @author HezaoHezao
 */
public interface IdempotentKeyGenerator {

    /**
     * 生成幂等Key
     *
     * @param prefix 前缀
     * @param point  切点
     * @return 幂等Key
     */
    String generateKey(String prefix, ProceedingJoinPoint point);
}