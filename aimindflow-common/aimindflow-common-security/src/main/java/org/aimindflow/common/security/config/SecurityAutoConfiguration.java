package org.aimindflow.common.security.config;

import org.aimindflow.common.security.properties.SecurityProperties;
import org.aimindflow.common.security.utils.JwtUtils;
import org.aimindflow.common.security.utils.PasswordUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全自动配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@ConditionalOnProperty(prefix = "aimindflow.security", name = "enabled", havingValue = "true", matchIfMissing = true)
public class SecurityAutoConfiguration {

    /**
     * 密码编码器
     *
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT工具类
     *
     * @return JwtUtils
     */
    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }

    /**
     * 密码工具类
     *
     * @return PasswordUtils
     */
    @Bean
    public PasswordUtils passwordUtils() {
        return new PasswordUtils();
    }
}