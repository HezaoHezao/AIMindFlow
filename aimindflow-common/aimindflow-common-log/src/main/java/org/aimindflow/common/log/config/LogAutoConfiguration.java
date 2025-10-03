package org.aimindflow.common.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 日志模块自动配置
 *
 * 开启 AOP 并扫描日志相关组件，支持开关控制。
 */
@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(prefix = "aimindflow.log", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.log")
public class LogAutoConfiguration {
}