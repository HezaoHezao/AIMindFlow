package org.aimindflow.common.tenant.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import lombok.RequiredArgsConstructor;
import org.aimindflow.common.tenant.context.TenantContext;
import org.aimindflow.common.tenant.properties.TenantProperties;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.List;

/**
 * 租户处理器实现
 *
 * 负责提供租户ID表达式、租户列名以及忽略表的逻辑。
 *
 * @author HezaoHezao
 */
@RequiredArgsConstructor
public class TenantLineHandlerImpl implements TenantLineHandler {

    private final TenantProperties tenantProperties;

    /**
     * 返回当前线程的租户ID表达式
     */
    @Override
    public Expression getTenantId() {
        // 优先从线程上下文获取
        String tenantId = TenantContext.getTenantId();
        if (!StringUtils.hasText(tenantId)) {
            tenantId = tenantProperties.getDefaultTenantId();
        }
        return new StringValue(tenantId);
    }

    /**
     * 返回租户ID列名
     */
    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getTenantIdColumn();
    }

    /**
     * 指定哪些表忽略租户过滤
     */
    @Override
    public boolean ignoreTable(String tableName) {
        List<String> ignoreTables = tenantProperties.getIgnoreTables();
        return !CollectionUtils.isEmpty(ignoreTables) && ignoreTables.stream()
            .anyMatch(t -> t.equalsIgnoreCase(tableName));
    }
}