package org.aimindflow.common.mybatis.handler;

import org.aimindflow.common.mybatis.annotation.DataScope;

/**
 * 数据权限处理器
 *
 * @author HezaoHezao
 */
public interface DataScopeHandler {

    /**
     * 获取数据权限SQL
     *
     * @param dataScope 数据权限注解
     * @param deptAlias 部门别名
     * @param userAlias 用户别名
     * @return 数据权限SQL
     */
    String getSqlString(DataScope dataScope, String deptAlias, String userAlias);

    /**
     * 获取当前用户的部门ID
     *
     * @return 部门ID
     */
    Long getCurrentUserDeptId();

    /**
     * 获取当前用户ID
     *
     * @return 用户ID
     */
    Long getCurrentUserId();

    /**
     * 获取当前用户的数据权限范围
     *
     * @return 数据权限范围
     */
    String getDataScope();
}