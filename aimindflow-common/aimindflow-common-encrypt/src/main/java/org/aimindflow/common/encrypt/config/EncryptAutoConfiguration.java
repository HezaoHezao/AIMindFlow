package org.aimindflow.common.encrypt.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 加密模块自动配置
 *
 * 扫描加密相关组件，支持开关控制。
 */
@Configuration
@ConditionalOnProperty(prefix = "aimindflow.encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.encrypt")
public class EncryptAutoConfiguration {
}