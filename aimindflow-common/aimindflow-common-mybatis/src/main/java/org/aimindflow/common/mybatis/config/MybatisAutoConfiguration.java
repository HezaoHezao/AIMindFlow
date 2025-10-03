package org.aimindflow.common.mybatis.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * MyBatis 模块自动配置
 *
 * 导入 MyBatisPlus 与动态数据源相关配置，按需启用。
 */
@Configuration
@ConditionalOnClass(name = "com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor")
@ConditionalOnProperty(prefix = "aimindflow.mybatis", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({MybatisConfig.class, DynamicDataSourceConfig.class})
public class MybatisAutoConfiguration {
}