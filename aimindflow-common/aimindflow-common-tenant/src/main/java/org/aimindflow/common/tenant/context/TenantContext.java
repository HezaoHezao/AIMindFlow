package org.aimindflow.common.tenant.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 租户上下文
 *
 * @author HezaoHezao
 */
@Slf4j
public class TenantContext {

    /**
     * 租户ID存储
     */
    private static final ThreadLocal<String> TENANT_ID_HOLDER = new ThreadLocal<>();

    /**
     * 是否忽略租户ID存储
     */
    private static final ThreadLocal<Boolean> IGNORE_TENANT_HOLDER = new ThreadLocal<>();

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        if (StringUtils.hasText(tenantId)) {
            TENANT_ID_HOLDER.set(tenantId);
            log.debug("设置租户ID: {}", tenantId);
        }
    }

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public static String getTenantId() {
        return TENANT_ID_HOLDER.get();
    }

    /**
     * 清除租户ID
     */
    public static void clear() {
        TENANT_ID_HOLDER.remove();
        IGNORE_TENANT_HOLDER.remove();
        log.debug("清除租户上下文");
    }

    /**
     * 设置忽略租户ID
     *
     * @param ignore 是否忽略
     */
    public static void setIgnore(boolean ignore) {
        IGNORE_TENANT_HOLDER.set(ignore);
        log.debug("设置忽略租户ID: {}", ignore);
    }

    /**
     * 是否忽略租户ID
     *
     * @return 是否忽略
     */
    public static boolean isIgnore() {
        Boolean ignore = IGNORE_TENANT_HOLDER.get();
        return ignore != null && ignore;
    }

    /**
     * 执行忽略租户ID的操作
     *
     * @param runnable 要执行的操作
     */
    public static void runIgnore(Runnable runnable) {
        boolean originalIgnore = isIgnore();
        try {
            setIgnore(true);
            runnable.run();
        } finally {
            setIgnore(originalIgnore);
        }
    }

    /**
     * 执行指定租户ID的操作
     *
     * @param tenantId 租户ID
     * @param runnable 要执行的操作
     */
    public static void runWithTenant(String tenantId, Runnable runnable) {
        String originalTenantId = getTenantId();
        try {
            setTenantId(tenantId);
            runnable.run();
        } finally {
            if (originalTenantId != null) {
                setTenantId(originalTenantId);
            } else {
                clear();
            }
        }
    }

    /**
     * 检查是否有租户ID
     *
     * @return 是否有租户ID
     */
    public static boolean hasTenantId() {
        return StringUtils.hasText(getTenantId());
    }
}