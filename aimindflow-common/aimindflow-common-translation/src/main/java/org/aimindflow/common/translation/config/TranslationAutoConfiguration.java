package org.aimindflow.common.translation.config;

import org.aimindflow.common.translation.handler.TranslationResponseBodyAdvice;
import org.aimindflow.common.translation.properties.TranslationProperties;
import org.aimindflow.common.translation.service.TranslationService;
import org.aimindflow.common.translation.service.impl.DefaultTranslationService;
import org.aimindflow.common.translation.util.TranslationUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 翻译模块自动配置
 *
 * 提供默认的 TranslationService、TranslationUtils 与 TranslationResponseBodyAdvice。
 *
 * @author HezaoHezao
 */
@Configuration
@EnableConfigurationProperties(TranslationProperties.class)
public class TranslationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TranslationService translationService() {
        return new DefaultTranslationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public TranslationUtils translationUtils() {
        return new TranslationUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public TranslationResponseBodyAdvice translationResponseBodyAdvice() {
        return new TranslationResponseBodyAdvice();
    }
}