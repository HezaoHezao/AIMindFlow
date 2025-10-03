package org.aimindflow.common.translation.annotation;

import java.lang.annotation.*;

/**
 * 字段翻译注解
 *
 * 用于对返回对象中的字段进行翻译处理，例如将字典编码、ID等原始值转换为可读的展示值。
 * 支持在 ResponseBodyAdvice 中统一递归处理。
 *
 * 使用示例：
 *  @Translation(type = "dict")
 *  private String status;
 *
 * @author HezaoHezao
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translation {
    /**
     * 翻译类型，例如：dict、user、dept 等
     */
    String type();

    /**
     * 备用键或字典类型，部分翻译器可能需要
     */
    String key() default "";
}