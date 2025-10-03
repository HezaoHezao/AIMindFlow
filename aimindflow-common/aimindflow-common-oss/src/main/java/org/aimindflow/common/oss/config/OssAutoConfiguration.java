package org.aimindflow.common.oss.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * OSS 模块自动配置
 *
 * 导入 OSS 配置，支持通过开关启用。
 */
@Configuration
@ConditionalOnProperty(prefix = "aimindflow.oss", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import(OssConfig.class)
public class OssAutoConfiguration {
}