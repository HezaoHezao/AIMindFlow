package org.aimindflow.common.sensitive.config;

import org.aimindflow.common.sensitive.handler.DefaultSensitiveHandler;
import org.aimindflow.common.sensitive.handler.SensitiveHandler;
import org.aimindflow.common.sensitive.handler.SensitiveResponseBodyAdvice;
import org.aimindflow.common.sensitive.properties.SensitiveProperties;
import org.aimindflow.common.sensitive.utils.SensitiveUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 敏感数据自动配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableConfigurationProperties(SensitiveProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.sensitive", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SensitiveAutoConfiguration {

    /**
     * 敏感数据处理器
     *
     * @return SensitiveHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public SensitiveHandler sensitiveHandler() {
        return new DefaultSensitiveHandler();
    }

    /**
     * 敏感数据工具类
     *
     * @return SensitiveUtils
     */
    @Bean
    @ConditionalOnMissingBean
    public SensitiveUtils sensitiveUtils() {
        return new SensitiveUtils();
    }

    /**
     * 响应体脱敏处理器
     *
     * @return SensitiveResponseBodyAdvice
     */
    @Bean
    @ConditionalOnProperty(prefix = "aimindflow.sensitive", name = "response-enabled", havingValue = "true", matchIfMissing = true)
    public SensitiveResponseBodyAdvice sensitiveResponseBodyAdvice() {
        return new SensitiveResponseBodyAdvice();
    }
}