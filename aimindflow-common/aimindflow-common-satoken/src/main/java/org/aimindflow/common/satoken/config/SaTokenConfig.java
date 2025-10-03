package org.aimindflow.common.satoken.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.stp.StpUtil;
import lombok.RequiredArgsConstructor;
import org.aimindflow.common.satoken.properties.SaTokenProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sa-Token配置类
 *
 * @author HezaoHezao
 */
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "aimindflow.sa-token", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SaTokenConfig implements WebMvcConfigurer {

    private final SaTokenProperties saTokenProperties;

    /**
     * 注册Sa-Token拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册Sa-Token拦截器，校验规则为StpUtil.checkLogin()登录校验
        registry.addInterceptor(new SaInterceptor(handle -> {
            SaRouter
                // 拦截所有路由
                .match("/**")
                // 排除登录接口
                .notMatch("/login", "/register", "/logout")
                // 排除静态资源
                .notMatch("/favicon.ico", "/error")
                // 排除Swagger相关接口
                .notMatch("/doc.html", "/webjars/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs/**")
                // 排除健康检查接口
                .notMatch("/actuator/**")
                // 排除自定义的排除路径
                .notMatch(saTokenProperties.getExcludePaths().toArray(new String[0]))
                // 执行认证函数
                .check(r -> StpUtil.checkLogin());
        })).addPathPatterns("/**");
    }

    /**
     * Sa-Token整合JWT (Simple模式)
     */
    @Bean
    @ConditionalOnProperty(prefix = "aimindflow.sa-token", name = "jwt-mode", havingValue = "true")
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }
}