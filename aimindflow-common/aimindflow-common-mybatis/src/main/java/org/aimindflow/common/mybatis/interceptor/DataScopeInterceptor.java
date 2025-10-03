package org.aimindflow.common.mybatis.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.aimindflow.common.mybatis.annotation.DataScope;
import org.aimindflow.common.mybatis.handler.DataScopeHandler;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.Map;

/**
 * 数据权限拦截器
 *
 * @author HezaoHezao
 */
@Slf4j
public class DataScopeInterceptor implements InnerInterceptor {

    private final DataScopeHandler dataScopeHandler;

    public DataScopeInterceptor(DataScopeHandler dataScopeHandler) {
        this.dataScopeHandler = dataScopeHandler;
    }

    @Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 获取当前执行的方法
        String mappedStatementId = ms.getId();
        String className = mappedStatementId.substring(0, mappedStatementId.lastIndexOf("."));
        String methodName = mappedStatementId.substring(mappedStatementId.lastIndexOf(".") + 1);

        // 获取数据权限注解
        DataScope dataScope = getDataScopeAnnotation(className, methodName);
        if (dataScope == null || !dataScope.enabled()) {
            return;
        }

        // 获取数据权限SQL
        String sqlString = dataScopeHandler.getSqlString(dataScope, dataScope.deptAlias(), dataScope.userAlias());
        if (sqlString == null || sqlString.isEmpty()) {
            return;
        }

        // 获取原始SQL
        PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
        String originalSql = mpBoundSql.sql();

        // 构建新的SQL
        try {
            Select select = (Select) CCJSqlParserUtil.parse(originalSql);
            PlainSelect plainSelect = (PlainSelect) select.getSelectBody();

            Expression whereExpression = plainSelect.getWhere();
            if (whereExpression == null) {
                // 如果没有WHERE子句，则添加WHERE子句
                sqlString = sqlString.replaceFirst("AND", "WHERE");
            }

            // 拼接新SQL
            String newSql = originalSql + sqlString;
            mpBoundSql.sql(newSql);
        } catch (JSQLParserException e) {
            log.error("数据权限拦截器解析SQL异常", e);
        }
    }

    /**
     * 获取数据权限注解
     *
     * @param className  类名
     * @param methodName 方法名
     * @return 数据权限注解
     */
    private DataScope getDataScopeAnnotation(String className, String methodName) {
        try {
            Class<?> clazz = Class.forName(className);
            // 优先获取方法上的注解
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName)) {
                    DataScope dataScope = method.getAnnotation(DataScope.class);
                    if (dataScope != null) {
                        return dataScope;
                    }
                }
            }
            // 获取类上的注解
            return clazz.getAnnotation(DataScope.class);
        } catch (ClassNotFoundException e) {
            log.error("获取数据权限注解异常", e);
            return null;
        }
    }
}