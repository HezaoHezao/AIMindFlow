package org.aimindflow.common.tenant.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置属性
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "aimindflow.tenant")
public class TenantProperties {

    /**
     * 是否启用多租户
     */
    private boolean enabled = false;

    /**
     * 租户ID字段名
     */
    private String tenantIdColumn = "tenant_id";

    /**
     * 租户ID请求头名称
     */
    private String tenantIdHeader = "Tenant-Id";

    /**
     * 租户ID参数名称
     */
    private String tenantIdParam = "tenantId";

    /**
     * 默认租户ID
     */
    private String defaultTenantId = "000000";

    /**
     * 是否忽略租户ID为空的情况
     */
    private boolean ignoreTenantIdEmpty = true;

    /**
     * 忽略多租户的表名列表
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 忽略多租户的SQL ID列表
     */
    private List<String> ignoreSqlIds = new ArrayList<>();

    /**
     * 是否在开发环境禁用多租户
     */
    private boolean disableInDev = true;

    /**
     * 租户数据源配置
     */
    private DataSource dataSource = new DataSource();

    /**
     * 数据源配置
     */
    @Data
    public static class DataSource {
        /**
         * 是否启用动态数据源
         */
        private boolean enabled = false;

        /**
         * 数据源前缀
         */
        private String prefix = "tenant_";

        /**
         * 默认数据源名称
         */
        private String defaultDataSource = "master";
    }
}