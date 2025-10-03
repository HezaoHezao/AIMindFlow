package org.aimindflow.common.excel.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Excel 模块自动配置
 *
 * 提供 Excel 组件扫描并可通过配置开关控制。
 */
@Configuration
@ConditionalOnProperty(prefix = "aimindflow.excel", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.excel")
public class ExcelAutoConfiguration {
}