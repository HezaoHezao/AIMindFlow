package org.aimindflow.common.log.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 日志配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableAspectJAutoProxy
@ComponentScan("org.aimindflow.common.log")
public class LogConfig {
    
}