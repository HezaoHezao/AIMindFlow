package org.aimindflow.common.tenant.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.tenant.handler.TenantLineHandlerImpl;
import org.aimindflow.common.tenant.interceptor.TenantInterceptor;
import org.aimindflow.common.tenant.properties.TenantProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 多租户自动配置类
 *
 * @author HezaoHezao
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.tenant", name = "enabled", havingValue = "true")
@ConditionalOnClass(MybatisPlusInterceptor.class)
@AutoConfigureAfter(name = "org.aimindflow.common.mybatis.config.MybatisConfig")
public class TenantAutoConfiguration implements WebMvcConfigurer {

    /**
     * 租户处理器
     *
     * @param tenantProperties 租户配置属性
     * @return TenantLineHandlerImpl
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantLineHandlerImpl tenantLineHandler(TenantProperties tenantProperties) {
        log.info("初始化租户处理器");
        return new TenantLineHandlerImpl(tenantProperties);
    }

    /**
     * 租户拦截器
     *
     * @param tenantLineHandler 租户处理器
     * @return TenantLineInnerInterceptor
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantLineHandlerImpl tenantLineHandler) {
        log.info("初始化租户SQL拦截器");
        return new TenantLineInnerInterceptor(tenantLineHandler);
    }

    /**
     * 配置租户拦截器到MyBatis Plus
     *
     * @param mybatisPlusInterceptor MyBatis Plus拦截器
     * @param tenantLineInnerInterceptor 租户内部拦截器
     */
    @Bean
    @ConditionalOnBean({MybatisPlusInterceptor.class, TenantLineInnerInterceptor.class})
    public MybatisPlusInterceptor configureTenantInterceptor(
            MybatisPlusInterceptor mybatisPlusInterceptor,
            TenantLineInnerInterceptor tenantLineInnerInterceptor) {
        
        // 将租户拦截器添加到第一位，确保在其他拦截器之前执行
        mybatisPlusInterceptor.getInterceptors().add(0, tenantLineInnerInterceptor);
        log.info("租户拦截器已添加到MyBatis Plus拦截器链");
        return mybatisPlusInterceptor;
    }

    @Autowired(required = false)
    private TenantInterceptor tenantInterceptor;

    /**
     * 提供租户HTTP拦截器Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantInterceptor tenantHttpInterceptor() {
        return new TenantInterceptor();
    }

    /**
     * 注册租户拦截器到Web MVC
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (tenantInterceptor != null) {
            registry.addInterceptor(tenantInterceptor)
                    .addPathPatterns("/**")
                    .order(1); // 设置较高优先级
            log.info("租户HTTP拦截器已注册到Web MVC");
        } else {
            log.warn("租户HTTP拦截器未注入，跳过注册");
        }
    }
}