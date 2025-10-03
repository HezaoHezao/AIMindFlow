package org.aimindflow.common.chat.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 聊天配置类
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.chat")
public class ChatConfig {

    /**
     * API密钥
     */
    private String apiKey;

    /**
     * 默认模型
     */
    private String defaultModel = "gpt-3.5-turbo";

    /**
     * 默认温度参数
     */
    private Double defaultTemperature = 0.7;

    /**
     * 默认最大生成的token数
     */
    private Integer defaultMaxTokens = 2048;

    /**
     * 连接超时时间（秒）
     */
    private Integer connectTimeout = 30;

    /**
     * 写入超时时间（秒）
     */
    private Integer writeTimeout = 30;

    /**
     * 读取超时时间（秒）
     */
    private Integer readTimeout = 60;

    /**
     * 代理主机
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private Integer proxyPort;

    /**
     * 代理用户名
     */
    private String proxyUsername;

    /**
     * 代理密码
     */
    private String proxyPassword;
}