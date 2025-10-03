package org.aimindflow.common.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全配置属性
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.security")
public class SecurityProperties {

    /**
     * 是否启用安全模块
     */
    private Boolean enabled = true;

    /**
     * JWT配置
     */
    private Jwt jwt = new Jwt();

    /**
     * 密码配置
     */
    private Password password = new Password();

    /**
     * 排除路径，不进行安全校验
     */
    private List<String> excludePaths = new ArrayList<>();

    /**
     * JWT配置类
     */
    @Data
    public static class Jwt {
        /**
         * JWT密钥
         */
        private String secret = "aimindflow-jwt-secret-key-2024";

        /**
         * JWT过期时间（秒）
         */
        private Long expiration = 7200L;

        /**
         * JWT刷新时间（秒）
         */
        private Long refreshExpiration = 604800L;

        /**
         * JWT请求头名称
         */
        private String header = "Authorization";

        /**
         * JWT令牌前缀
         */
        private String tokenPrefix = "Bearer ";

        /**
         * 是否启用JWT
         */
        private Boolean enabled = true;
    }

    /**
     * 密码配置类
     */
    @Data
    public static class Password {
        /**
         * 密码最小长度
         */
        private Integer minLength = 6;

        /**
         * 密码最大长度
         */
        private Integer maxLength = 20;

        /**
         * 是否需要包含数字
         */
        private Boolean requireDigit = true;

        /**
         * 是否需要包含字母
         */
        private Boolean requireLetter = true;

        /**
         * 是否需要包含特殊字符
         */
        private Boolean requireSpecialChar = false;

        /**
         * 密码加密强度（BCrypt rounds）
         */
        private Integer strength = 10;
    }
}