package org.aimindflow.common.satoken.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token属性配置
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.sa-token")
public class SaTokenProperties {

    /**
     * 是否启用Sa-Token
     */
    private Boolean enabled = true;

    /**
     * 是否启用JWT模式
     */
    private Boolean jwtMode = false;

    /**
     * 排除路径，不进行认证校验
     */
    private List<String> excludePaths = new ArrayList<>();

    /**
     * 是否启用同端互斥登录
     */
    private Boolean isConcurrent = true;

    /**
     * 在多人登录同一账号时，是否共享一个token
     */
    private Boolean isShare = false;

    /**
     * 最大登录数量限制（-1表示不限制）
     */
    private Integer maxLoginCount = -1;

    /**
     * 是否尝试从请求体里读取token
     */
    private Boolean isReadBody = false;

    /**
     * 是否尝试从header里读取token
     */
    private Boolean isReadHeader = true;

    /**
     * 是否尝试从cookie里读取token
     */
    private Boolean isReadCookie = false;

    /**
     * token风格
     */
    private String tokenStyle = "uuid";

    /**
     * 数据持久化组件
     */
    private String dataSource = "redis";
}