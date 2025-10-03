package org.aimindflow.common.idempotent.generator.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.idempotent.annotation.Idempotent;
import org.aimindflow.common.idempotent.constant.IdempotentConstants;
import org.aimindflow.common.idempotent.generator.IdempotentKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 基于SpEL表达式的幂等Key生成器
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class SpelKeyGenerator implements IdempotentKeyGenerator {

    /**
     * SpEL表达式解析器
     */
    private final ExpressionParser parser = new SpelExpressionParser();

    /**
     * 参数名发现器
     */
    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    @Override
    public String generateKey(String prefix, ProceedingJoinPoint point) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取Idempotent注解
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        if (idempotent == null) {
            log.warn("方法{}未标注@Idempotent注解", method.getName());
            return null;
        }

        // 如果前缀为空，则使用方法名作为前缀
        if (!StringUtils.hasText(prefix)) {
            prefix = method.getDeclaringClass().getName() + "." + method.getName();
        }

        // 构建幂等Key
        StringBuilder keyBuilder = new StringBuilder(IdempotentConstants.REDIS_KEY_PREFIX)
                .append(prefix)
                .append(IdempotentConstants.KEY_SEPARATOR);

        try {
            // 获取方法参数名
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            if (parameterNames == null) {
                log.warn("无法获取方法{}的参数名", method.getName());
                return null;
            }

            // 创建SpEL上下文
            EvaluationContext context = new StandardEvaluationContext();
            Object[] args = point.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                context.setVariable(parameterNames[i], args[i]);
            }

            // 解析SpEL表达式
            Expression expression = parser.parseExpression(prefix);
            Object result = expression.getValue(context);
            if (result == null) {
                log.warn("SpEL表达式{}解析结果为null", prefix);
                return null;
            }

            // 对结果进行MD5处理，避免结果过长
            String resultMd5 = DigestUtils.md5DigestAsHex(result.toString().getBytes(StandardCharsets.UTF_8));
            keyBuilder.append(resultMd5);

            String key = keyBuilder.toString();
            log.debug("生成幂等Key: {}", key);
            return key;
        } catch (Exception e) {
            log.error("SpEL表达式解析异常", e);
            return null;
        }
    }
}