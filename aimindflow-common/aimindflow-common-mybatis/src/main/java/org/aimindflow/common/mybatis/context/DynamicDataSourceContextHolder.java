package org.aimindflow.common.mybatis.context;

/**
 * 动态数据源上下文持有者
 *
 * @author HezaoHezao
 */
public class DynamicDataSourceContextHolder {

    /**
     * 使用ThreadLocal维护变量，为每个线程提供独立的变量副本
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源类型
     *
     * @param dataSourceType 数据源类型
     */
    public static void setDataSourceType(String dataSourceType) {
        CONTEXT_HOLDER.set(dataSourceType);
    }

    /**
     * 获取数据源类型
     *
     * @return 数据源类型
     */
    public static String getDataSourceType() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除数据源类型
     */
    public static void clearDataSourceType() {
        CONTEXT_HOLDER.remove();
    }
}