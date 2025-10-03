package org.aimindflow.common.idempotent.generator.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.idempotent.constant.IdempotentConstants;
import org.aimindflow.common.idempotent.generator.IdempotentKeyGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 基于参数的幂等Key生成器
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class ParamsKeyGenerator implements IdempotentKeyGenerator {

    @Override
    public String generateKey(String prefix, ProceedingJoinPoint point) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String userKey = "";
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 获取用户标识，可以是用户ID、Token等
            userKey = request.getHeader("Authorization");
            if (!StringUtils.hasText(userKey)) {
                userKey = request.getRemoteAddr();
            }
        }

        // 如果前缀为空，则使用方法名作为前缀
        if (!StringUtils.hasText(prefix)) {
            prefix = method.getDeclaringClass().getName() + "." + method.getName();
        }

        // 构建幂等Key
        StringBuilder keyBuilder = new StringBuilder(IdempotentConstants.REDIS_KEY_PREFIX)
                .append(prefix)
                .append(IdempotentConstants.KEY_SEPARATOR);

        // 添加用户标识
        if (StringUtils.hasText(userKey)) {
            keyBuilder.append(userKey).append(IdempotentConstants.KEY_SEPARATOR);
        }

        // 添加方法参数
        String params = Arrays.toString(point.getArgs());
        // 对参数进行MD5处理，避免参数过长
        String paramsMd5 = DigestUtils.md5DigestAsHex(params.getBytes(StandardCharsets.UTF_8));
        keyBuilder.append(paramsMd5);

        String key = keyBuilder.toString();
        log.debug("生成幂等Key: {}", key);
        return key;
    }
}