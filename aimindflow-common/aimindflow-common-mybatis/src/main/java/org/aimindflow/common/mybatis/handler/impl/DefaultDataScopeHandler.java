package org.aimindflow.common.mybatis.handler.impl;

import org.aimindflow.common.mybatis.annotation.DataScope;
import org.aimindflow.common.mybatis.handler.DataScopeHandler;

/**
 * 默认数据权限处理器
 *
 * @author HezaoHezao
 */
public class DefaultDataScopeHandler implements DataScopeHandler {

    /**
     * 全部数据权限
     */
    private static final String DATA_SCOPE_ALL = "1";

    /**
     * 自定义数据权限
     */
    private static final String DATA_SCOPE_CUSTOM = "2";

    /**
     * 部门数据权限
     */
    private static final String DATA_SCOPE_DEPT = "3";

    /**
     * 部门及以下数据权限
     */
    private static final String DATA_SCOPE_DEPT_AND_CHILD = "4";

    /**
     * 仅本人数据权限
     */
    private static final String DATA_SCOPE_SELF = "5";

    @Override
    public String getSqlString(DataScope dataScope, String deptAlias, String userAlias) {
        StringBuilder sqlBuilder = new StringBuilder();

        // 如果不启用数据权限，则不进行过滤
        if (!dataScope.enabled()) {
            return sqlBuilder.toString();
        }

        // 获取当前用户的数据权限范围（字符串），避免与注解参数变量同名
        String dataScopeType = getDataScope();

        // 如果是全部数据权限，则不进行过滤
        if (DATA_SCOPE_ALL.equals(dataScopeType)) {
            return sqlBuilder.toString();
        }

        // 如果是仅本人数据权限或者注解中指定了仅查询本人数据
        if (DATA_SCOPE_SELF.equals(dataScopeType) || dataScope.self()) {
            if (userAlias != null && !userAlias.isEmpty()) {
                sqlBuilder.append(" AND ").append(userAlias).append(".user_id = ").append(getCurrentUserId());
            } else {
                sqlBuilder.append(" AND user_id = ").append(getCurrentUserId());
            }
            return sqlBuilder.toString();
        }

        // 如果部门别名为空，则不进行过滤
        if (deptAlias == null || deptAlias.isEmpty()) {
            return sqlBuilder.toString();
        }

        // 根据数据权限范围构建SQL
        if (DATA_SCOPE_CUSTOM.equals(dataScopeType)) {
            // 自定义数据权限
            sqlBuilder.append(" AND ").append(deptAlias).append(".dept_id IN (SELECT dept_id FROM sys_role_dept WHERE role_id = 1)");
        } else if (DATA_SCOPE_DEPT.equals(dataScopeType)) {
            // 本部门数据权限
            sqlBuilder.append(" AND ").append(deptAlias).append(".dept_id = ").append(getCurrentUserDeptId());
        } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(dataScopeType)) {
            // 本部门及以下数据权限
            sqlBuilder.append(" AND ").append(deptAlias).append(".dept_id IN (SELECT dept_id FROM sys_dept WHERE dept_id = ")
                    .append(getCurrentUserDeptId()).append(" OR ancestors LIKE '%,").append(getCurrentUserDeptId()).append(",%')");
        }

        return sqlBuilder.toString();
    }

    @Override
    public Long getCurrentUserDeptId() {
        // 默认实现，实际项目中需要根据具体的用户体系获取当前用户的部门ID
        return 1L;
    }

    @Override
    public Long getCurrentUserId() {
        // 默认实现，实际项目中需要根据具体的用户体系获取当前用户ID
        return 1L;
    }

    @Override
    public String getDataScope() {
        // 默认实现，实际项目中需要根据具体的用户体系获取当前用户的数据权限范围
        return DATA_SCOPE_ALL;
    }
}