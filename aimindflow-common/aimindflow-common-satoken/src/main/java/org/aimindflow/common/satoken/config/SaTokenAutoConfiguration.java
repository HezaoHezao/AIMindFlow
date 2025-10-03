package org.aimindflow.common.satoken.config;

import org.aimindflow.common.satoken.aspect.SaTokenAspect;
import org.aimindflow.common.satoken.properties.SaTokenProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Sa-Token自动配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableConfigurationProperties(SaTokenProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.sa-token", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(SaTokenConfig.class)
public class SaTokenAutoConfiguration {

    /**
     * Sa-Token权限校验切面
     */
    @Bean
    public SaTokenAspect saTokenAspect() {
        return new SaTokenAspect();
    }
}