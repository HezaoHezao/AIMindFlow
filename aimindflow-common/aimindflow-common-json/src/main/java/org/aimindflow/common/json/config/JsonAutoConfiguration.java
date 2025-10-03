package org.aimindflow.common.json.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * JSON 模块自动配置
 *
 * 扫描 JSON 相关组件，支持开关控制。
 */
@Configuration
@ConditionalOnProperty(prefix = "aimindflow.json", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.json")
public class JsonAutoConfiguration {
}