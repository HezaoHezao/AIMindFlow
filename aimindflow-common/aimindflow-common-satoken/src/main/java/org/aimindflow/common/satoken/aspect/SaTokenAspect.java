package org.aimindflow.common.satoken.aspect;

import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.satoken.annotation.RequiresPermissions;
import org.aimindflow.common.satoken.annotation.RequiresRoles;
import org.aimindflow.common.satoken.enums.Logical;
import org.aimindflow.common.satoken.utils.LoginHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Sa-Token权限校验切面
 *
 * @author HezaoHezao
 */
@Slf4j
@Aspect
@Component
public class SaTokenAspect {

    /**
     * 构建
     */
    public SaTokenAspect() {
        log.info("Sa-Token权限校验切面 ==> 初始化完成");
    }

    /**
     * 设置权限校验切点
     */
    @Pointcut("@annotation(org.aimindflow.common.satoken.annotation.RequiresPermissions)" +
            " || @within(org.aimindflow.common.satoken.annotation.RequiresPermissions)")
    public void requiresPermissionsPointCut() {
    }

    /**
     * 设置角色校验切点
     */
    @Pointcut("@annotation(org.aimindflow.common.satoken.annotation.RequiresRoles)" +
            " || @within(org.aimindflow.common.satoken.annotation.RequiresRoles)")
    public void requiresRolesPointCut() {
    }

    /**
     * 权限校验
     */
    @Around("requiresPermissionsPointCut()")
    public Object requiresPermissions(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        RequiresPermissions requiresPermissions = getAnnotation(method, RequiresPermissions.class);
        if (requiresPermissions != null) {
            this.checkPermissions(requiresPermissions);
        }
        return joinPoint.proceed();
    }

    /**
     * 角色校验
     */
    @Around("requiresRolesPointCut()")
    public Object requiresRoles(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();
        RequiresRoles requiresRoles = getAnnotation(method, RequiresRoles.class);
        if (requiresRoles != null) {
            this.checkRoles(requiresRoles);
        }
        return joinPoint.proceed();
    }

    /**
     * 校验权限
     *
     * @param requiresPermissions 权限注解
     */
    private void checkPermissions(RequiresPermissions requiresPermissions) {
        String[] permissions = requiresPermissions.value();
        if (permissions.length == 0) {
            return;
        }

        if (permissions.length == 1) {
            if (!LoginHelper.hasPermi(permissions[0])) {
                throw new NotPermissionException(permissions[0]);
            }
            return;
        }

        if (Logical.AND.equals(requiresPermissions.logical())) {
            for (String permission : permissions) {
                if (!LoginHelper.hasPermi(permission)) {
                    throw new NotPermissionException(permission);
                }
            }
        } else {
            boolean hasPermission = false;
            for (String permission : permissions) {
                if (LoginHelper.hasPermi(permission)) {
                    hasPermission = true;
                    break;
                }
            }
            if (!hasPermission) {
                throw new NotPermissionException(permissions);
            }
        }
    }

    /**
     * 校验角色
     *
     * @param requiresRoles 角色注解
     */
    private void checkRoles(RequiresRoles requiresRoles) {
        String[] roles = requiresRoles.value();
        if (roles.length == 0) {
            return;
        }

        if (roles.length == 1) {
            if (!LoginHelper.hasRole(roles[0])) {
                throw new NotRoleException(roles[0]);
            }
            return;
        }

        if (Logical.AND.equals(requiresRoles.logical())) {
            for (String role : roles) {
                if (!LoginHelper.hasRole(role)) {
                    throw new NotRoleException(role);
                }
            }
        } else {
            boolean hasRole = false;
            for (String role : roles) {
                if (LoginHelper.hasRole(role)) {
                    hasRole = true;
                    break;
                }
            }
            if (!hasRole) {
                throw new NotRoleException(roles);
            }
        }
    }

    /**
     * 根据方法获取注解
     */
    private <T> T getAnnotation(Method method, Class<T> clazz) {
        // 先检查方法上的注解
        T annotation = AnnotationUtils.findAnnotation(method, clazz);
        if (annotation != null) {
            return annotation;
        }
        // 再检查类上的注解
        return AnnotationUtils.findAnnotation(method.getDeclaringClass(), clazz);
    }
}