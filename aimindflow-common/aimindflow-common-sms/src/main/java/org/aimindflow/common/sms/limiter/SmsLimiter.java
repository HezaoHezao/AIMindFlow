package org.aimindflow.common.sms.limiter;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sms.properties.SmsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 短信发送限流器
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class SmsLimiter {

    @Autowired
    private SmsProperties smsProperties;

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    private static final String SMS_LIMIT_PREFIX = "sms:limit:";
    private static final String IP_LIMIT_PREFIX = "sms:ip:limit:";

    /**
     * 检查手机号发送限制
     *
     * @param phoneNumber 手机号码
     * @return 是否允许发送
     */
    public boolean checkPhoneLimit(String phoneNumber) {
        if (!smsProperties.getLimit().isEnabled()) {
            return true;
        }

        if (redisTemplate == null) {
            log.warn("Redis未配置，跳过短信限流检查");
            return true;
        }

        try {
            // 检查每分钟限制
            if (!checkLimit(SMS_LIMIT_PREFIX + phoneNumber + ":minute", 
                    smsProperties.getLimit().getMaxPerMinute(), 60)) {
                log.warn("手机号 {} 每分钟发送次数超限", phoneNumber);
                return false;
            }

            // 检查每小时限制
            if (!checkLimit(SMS_LIMIT_PREFIX + phoneNumber + ":hour", 
                    smsProperties.getLimit().getMaxPerHour(), 3600)) {
                log.warn("手机号 {} 每小时发送次数超限", phoneNumber);
                return false;
            }

            // 检查每天限制
            if (!checkLimit(SMS_LIMIT_PREFIX + phoneNumber + ":day", 
                    smsProperties.getLimit().getMaxPerDay(), 86400)) {
                log.warn("手机号 {} 每天发送次数超限", phoneNumber);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("检查手机号限制异常", e);
            // 异常情况下允许发送
            return true;
        }
    }

    /**
     * 检查IP发送限制
     *
     * @param ipAddress IP地址
     * @return 是否允许发送
     */
    public boolean checkIpLimit(String ipAddress) {
        if (!smsProperties.getLimit().isEnabled() || !StringUtils.hasText(ipAddress)) {
            return true;
        }

        if (redisTemplate == null) {
            log.warn("Redis未配置，跳过IP限流检查");
            return true;
        }

        try {
            // 检查IP每分钟限制
            if (!checkLimit(IP_LIMIT_PREFIX + ipAddress + ":minute", 
                    smsProperties.getLimit().getMaxPerIpMinute(), 60)) {
                log.warn("IP {} 每分钟发送次数超限", ipAddress);
                return false;
            }

            // 检查IP每小时限制
            if (!checkLimit(IP_LIMIT_PREFIX + ipAddress + ":hour", 
                    smsProperties.getLimit().getMaxPerIpHour(), 3600)) {
                log.warn("IP {} 每小时发送次数超限", ipAddress);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("检查IP限制异常", e);
            // 异常情况下允许发送
            return true;
        }
    }

    /**
     * 记录发送次数
     *
     * @param phoneNumber 手机号码
     * @param ipAddress   IP地址
     */
    public void recordSend(String phoneNumber, String ipAddress) {
        if (!smsProperties.getLimit().isEnabled() || redisTemplate == null) {
            return;
        }

        try {
            // 记录手机号发送次数
            incrementCounter(SMS_LIMIT_PREFIX + phoneNumber + ":minute", 60);
            incrementCounter(SMS_LIMIT_PREFIX + phoneNumber + ":hour", 3600);
            incrementCounter(SMS_LIMIT_PREFIX + phoneNumber + ":day", 86400);

            // 记录IP发送次数
            if (StringUtils.hasText(ipAddress)) {
                incrementCounter(IP_LIMIT_PREFIX + ipAddress + ":minute", 60);
                incrementCounter(IP_LIMIT_PREFIX + ipAddress + ":hour", 3600);
            }
        } catch (Exception e) {
            log.error("记录发送次数异常", e);
        }
    }

    /**
     * 检查限制
     *
     * @param key       Redis键
     * @param maxCount  最大次数
     * @param expireSeconds 过期时间（秒）
     * @return 是否允许
     */
    private boolean checkLimit(String key, int maxCount, int expireSeconds) {
        if (maxCount <= 0) {
            return true;
        }

        try {
            Long count = redisTemplate.opsForValue().increment(key);
            if (count == 1) {
                // 第一次设置过期时间
                redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));
            }
            return count <= maxCount;
        } catch (Exception e) {
            log.error("检查限制异常，key: {}", key, e);
            return true;
        }
    }

    /**
     * 增加计数器
     *
     * @param key           Redis键
     * @param expireSeconds 过期时间（秒）
     */
    private void incrementCounter(String key, int expireSeconds) {
        try {
            Long count = redisTemplate.opsForValue().increment(key);
            if (count == 1) {
                // 第一次设置过期时间
                redisTemplate.expire(key, Duration.ofSeconds(expireSeconds));
            }
        } catch (Exception e) {
            log.error("增加计数器异常，key: {}", key, e);
        }
    }

    /**
     * 清除手机号限制
     *
     * @param phoneNumber 手机号码
     */
    public void clearPhoneLimit(String phoneNumber) {
        if (redisTemplate == null) {
            return;
        }

        try {
            redisTemplate.delete(SMS_LIMIT_PREFIX + phoneNumber + ":minute");
            redisTemplate.delete(SMS_LIMIT_PREFIX + phoneNumber + ":hour");
            redisTemplate.delete(SMS_LIMIT_PREFIX + phoneNumber + ":day");
            log.info("清除手机号 {} 的发送限制", phoneNumber);
        } catch (Exception e) {
            log.error("清除手机号限制异常", e);
        }
    }

    /**
     * 清除IP限制
     *
     * @param ipAddress IP地址
     */
    public void clearIpLimit(String ipAddress) {
        if (redisTemplate == null || !StringUtils.hasText(ipAddress)) {
            return;
        }

        try {
            redisTemplate.delete(IP_LIMIT_PREFIX + ipAddress + ":minute");
            redisTemplate.delete(IP_LIMIT_PREFIX + ipAddress + ":hour");
            log.info("清除IP {} 的发送限制", ipAddress);
        } catch (Exception e) {
            log.error("清除IP限制异常", e);
        }
    }
}