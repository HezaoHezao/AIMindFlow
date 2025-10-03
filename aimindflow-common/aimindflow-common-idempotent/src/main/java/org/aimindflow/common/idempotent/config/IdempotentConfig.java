package org.aimindflow.common.idempotent.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 幂等配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"org.aimindflow.common.idempotent"})
public class IdempotentConfig {
    // 可以在这里添加幂等相关的配置
}