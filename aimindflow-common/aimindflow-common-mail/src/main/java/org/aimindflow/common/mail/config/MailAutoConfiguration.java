package org.aimindflow.common.mail.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 邮件模块自动配置
 *
 * 扫描邮件相关组件，支持通过配置开关启用。
 */
@Configuration
@ConditionalOnProperty(prefix = "aimindflow.mail", name = "enabled", havingValue = "true", matchIfMissing = true)
@ComponentScan("org.aimindflow.common.mail")
public class MailAutoConfiguration {
}