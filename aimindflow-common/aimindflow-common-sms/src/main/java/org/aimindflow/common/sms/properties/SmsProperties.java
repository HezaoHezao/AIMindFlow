package org.aimindflow.common.sms.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 短信配置属性
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.sms")
public class SmsProperties {

    /**
     * 是否启用短信服务
     */
    private Boolean enabled = true;

    /**
     * 短信服务提供商类型
     */
    private ProviderType provider = ProviderType.ALIYUN;

    /**
     * 阿里云短信配置
     */
    private Aliyun aliyun = new Aliyun();

    /**
     * 腾讯云短信配置
     */
    private Tencent tencent = new Tencent();

    /**
     * 短信发送限制配置
     */
    private Limit limit = new Limit();

    /**
     * 短信服务提供商类型枚举
     */
    public enum ProviderType {
        /**
         * 阿里云
         */
        ALIYUN,
        /**
         * 腾讯云
         */
        TENCENT
    }

    /**
     * 阿里云短信配置
     */
    @Data
    public static class Aliyun {
        /**
         * AccessKey ID
         */
        private String accessKeyId;

        /**
         * AccessKey Secret
         */
        private String accessKeySecret;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 地域节点
         */
        private String regionId = "cn-hangzhou";

        /**
         * 端点
         */
        private String endpoint = "dysmsapi.aliyuncs.com";
    }

    /**
     * 腾讯云短信配置
     */
    @Data
    public static class Tencent {
        /**
         * SecretId
         */
        private String secretId;

        /**
         * SecretKey
         */
        private String secretKey;

        /**
         * 短信应用ID
         */
        private String appId;

        /**
         * 短信签名
         */
        private String signName;

        /**
         * 地域
         */
        private String region = "ap-beijing";
    }

    /**
     * 短信发送限制配置
     */
    @Data
    public static class Limit {
        /**
         * 是否启用发送限制
         */
        private Boolean enabled = true;

        /**
         * 同一手机号每分钟最大发送次数
         */
        private Integer maxPerMinute = 1;

        /**
         * 同一手机号每小时最大发送次数
         */
        private Integer maxPerHour = 5;

        /**
         * 同一手机号每天最大发送次数
         */
        private Integer maxPerDay = 10;

        /**
         * 同一IP每分钟最大发送次数
         */
        private Integer maxPerIpMinute = 10;

        /**
         * 同一IP每小时最大发送次数
         */
        private Integer maxPerIpHour = 100;
    }
}