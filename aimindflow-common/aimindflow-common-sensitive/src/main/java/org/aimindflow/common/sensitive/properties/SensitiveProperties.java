package org.aimindflow.common.sensitive.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 敏感数据配置属性
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.sensitive")
public class SensitiveProperties {

    /**
     * 是否启用敏感数据处理
     */
    private Boolean enabled = true;

    /**
     * 是否启用响应体脱敏
     */
    private Boolean responseEnabled = true;

    /**
     * 是否启用日志脱敏
     */
    private Boolean logEnabled = true;

    /**
     * 默认脱敏字符
     */
    private String defaultMaskChar = "*";

    /**
     * 是否在开发环境禁用脱敏
     */
    private Boolean disableInDev = false;
}