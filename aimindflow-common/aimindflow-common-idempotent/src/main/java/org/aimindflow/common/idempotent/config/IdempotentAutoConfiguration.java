package org.aimindflow.common.idempotent.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 幂等模块自动配置
 *
 * 开启 AOP 并扫描幂等相关组件，支持开关控制。
 */
@Configuration
@EnableAspectJAutoProxy
@ConditionalOnProperty(prefix = "aimindflow.idempotent", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.idempotent")
public class IdempotentAutoConfiguration {
}