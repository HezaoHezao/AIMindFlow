package org.aimindflow.common.sms.config;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sms.limiter.SmsLimiter;
import org.aimindflow.common.sms.properties.SmsProperties;
import org.aimindflow.common.sms.service.SmsService;
import org.aimindflow.common.sms.service.impl.AliyunSmsServiceImpl;
import org.aimindflow.common.sms.service.impl.TencentSmsServiceImpl;
import org.aimindflow.common.sms.utils.SmsUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 短信服务自动配置类
 *
 * @author HezaoHezao
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.sms", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({AliyunSmsServiceImpl.class, TencentSmsServiceImpl.class})
public class SmsAutoConfiguration {

    /**
     * 短信限流器
     *
     * @param smsProperties 短信配置属性
     * @return SmsLimiter
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsLimiter smsLimiter(SmsProperties smsProperties) {
        log.info("初始化短信限流器");
        return new SmsLimiter();
    }

    /**
     * 短信工具类
     *
     * @param smsService 短信服务
     * @param smsLimiter 短信限流器
     * @return SmsUtils
     */
    @Bean
    @ConditionalOnMissingBean
    public SmsUtils smsUtils(SmsService smsService, SmsLimiter smsLimiter) {
        log.info("初始化短信工具类");
        return new SmsUtils();
    }
}