package org.aimindflow.common.ratelimiter.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.ratelimiter.RateLimiter;
import org.aimindflow.common.ratelimiter.annotation.RateLimit;
import org.aimindflow.common.ratelimiter.enums.LimitType;
import org.aimindflow.common.ratelimiter.exception.RateLimitException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 限流切面
 *
 * @author HezaoHezao
 */
@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class RateLimitAspect {

    /**
     * 限流器，默认使用Redis限流器
     */
    @Autowired
    @Qualifier("redisRateLimiter")
    private RateLimiter rateLimiter;

    /**
     * 应用名称
     */
    @Value("${spring.application.name:application}")
    private String applicationName;

    /**
     * 限流前置通知
     */
    @Before("@annotation(org.aimindflow.common.ratelimiter.annotation.RateLimit)")
    public void doBefore(JoinPoint point) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();

        // 获取限流注解
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return;
        }

        // 获取限流key
        String key = getLimitKey(rateLimit, method);

        // 尝试获取令牌
        boolean acquired = rateLimiter.tryAcquire(key, rateLimit.count(), rateLimit.time());
        if (!acquired) {
            log.warn("限流拦截: {}", key);
            throw new RateLimitException(rateLimit.message());
        }
    }

    /**
     * 获取限流key
     *
     * @param rateLimit 限流注解
     * @param method    方法
     * @return 限流key
     */
    private String getLimitKey(RateLimit rateLimit, Method method) {
        StringBuilder key = new StringBuilder("rate_limit:");
        key.append(applicationName).append(":");

        // 如果设置了自定义key，则使用自定义key
        if (rateLimit.key().length() > 0) {
            key.append(rateLimit.key());
            return key.toString();
        }

        // 默认使用类名+方法名作为key
        key.append(method.getDeclaringClass().getName()).append(".").append(method.getName());

        // 根据限流类型追加不同的key
        if (rateLimit.limitType() == LimitType.IP) {
            key.append(":").append(getIpAddress());
        } else if (rateLimit.limitType() == LimitType.USER) {
            // 这里可以根据实际情况获取用户ID
            // 例如从请求头、Session或ThreadLocal中获取
            String userId = getUserId();
            if (userId != null) {
                key.append(":").append(userId);
            }
        }

        return key.toString();
    }

    /**
     * 获取IP地址
     *
     * @return IP地址
     */
    private String getIpAddress() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }

        return ip;
    }

    /**
     * 获取当前请求
     *
     * @return 当前请求
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes == null ? null : attributes.getRequest();
    }

    /**
     * 获取用户ID
     * 注意：这里需要根据实际项目情况获取用户ID
     *
     * @return 用户ID
     */
    private String getUserId() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }

        // 这里可以根据实际情况从请求头、Session或ThreadLocal中获取用户ID
        // 例如：从请求头中获取
        return request.getHeader("X-User-Id");
    }
}