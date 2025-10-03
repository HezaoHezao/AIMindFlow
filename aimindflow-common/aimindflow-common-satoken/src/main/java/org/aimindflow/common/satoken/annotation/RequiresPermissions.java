package org.aimindflow.common.satoken.annotation;

import org.aimindflow.common.satoken.enums.Logical;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 * 可标注在函数、类上（效果等同于标注在此类的所有方法上）
 *
 * @author HezaoHezao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresPermissions {

    /**
     * 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 验证逻辑：AND | OR，默认AND
     */
    Logical logical() default Logical.AND;
}