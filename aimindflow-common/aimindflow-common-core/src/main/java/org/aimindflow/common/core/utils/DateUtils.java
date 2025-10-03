package org.aimindflow.common.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    /**
     * 返回日期路径，例如：yyyy/MM/dd
     */
    public static String datePath() {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

    /**
     * 返回当前时间字符串，例如：yyyy-MM-dd HH:mm:ss
     */
    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}