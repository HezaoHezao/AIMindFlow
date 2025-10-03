package org.aimindflow.common.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel字段注解，用于标记导出到Excel的字段
 *
 * @author HezaoHezao
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 字段名称
     */
    String name() default "";

    /**
     * 字段顺序
     */
    int order() default 0;

    /**
     * 日期格式，如：yyyy-MM-dd
     */
    String dateFormat() default "";

    /**
     * 是否导出
     */
    boolean isExport() default true;

    /**
     * 是否导入
     */
    boolean isImport() default true;

    /**
     * 宽度
     */
    int width() default 0;

    /**
     * 是否自动调整宽度
     */
    boolean isAutoSize() default true;

    /**
     * 是否必填
     */
    boolean required() default false;

    /**
     * 数据字典，格式：0=男,1=女
     */
    String dict() default "";
}