package org.aimindflow.common.mybatis.utils;

import org.aimindflow.common.core.exception.BaseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SQL过滤工具类
 *
 * @author HezaoHezao
 */
public class SqlFilterUtils {

    /**
     * SQL注入过滤
     */
    private static final String SQL_INJECT_PATTERN = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|"  
            + "(\\b(select|update|delete|insert|truncate|drop|alter|exec|execute|call|declare|create)\\b)"; 

    /**
     * 检查是否包含SQL注入风险
     *
     * @param value 需要检查的值
     * @return 是否包含SQL注入风险
     */
    public static boolean hasSqlInject(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        
        Pattern pattern = Pattern.compile(SQL_INJECT_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(value);
        return matcher.find();
    }

    /**
     * 检查是否包含SQL注入风险，如果存在则抛出异常
     *
     * @param value 需要检查的值
     */
    public static void checkSqlInject(String value) {
        if (hasSqlInject(value)) {
            throw new BaseException("包含SQL注入风险");
        }
    }

    /**
     * 过滤SQL注入风险
     *
     * @param value 需要过滤的值
     * @return 过滤后的值
     */
    public static String filterSqlInject(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        // 替换SQL注入关键字
        return value.replaceAll(SQL_INJECT_PATTERN, "");
    }
}