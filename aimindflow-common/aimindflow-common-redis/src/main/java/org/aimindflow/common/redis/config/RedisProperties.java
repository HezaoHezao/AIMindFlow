package org.aimindflow.common.redis.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Redis属性配置
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.redis")
public class RedisProperties {

    /**
     * 是否启用Redis，默认启用
     */
    private boolean enabled = true;

    /**
     * 缓存前缀
     */
    private String cachePrefix = "aimindflow:";

    /**
     * 默认过期时间（秒）
     */
    private long defaultExpiration = 3600;
}