package org.aimindflow.common.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Web模块自动配置
 *
 * 提供 Web 相关的基础配置与组件扫描。
 *
 * @author HezaoHezao
 */
@Configuration
@ComponentScan("org.aimindflow.common.web")
public class WebAutoConfiguration {
}