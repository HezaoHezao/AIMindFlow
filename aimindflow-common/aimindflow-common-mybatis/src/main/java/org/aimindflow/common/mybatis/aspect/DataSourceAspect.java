package org.aimindflow.common.mybatis.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.mybatis.annotation.DataSource;
import org.aimindflow.common.mybatis.context.DynamicDataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 多数据源切换处理
 *
 * @author HezaoHezao
 */
@Slf4j
@Aspect
@Order(1)
@Component
public class DataSourceAspect {

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(org.aimindflow.common.mybatis.annotation.DataSource)" +
            " || @within(org.aimindflow.common.mybatis.annotation.DataSource)")
    public void dataSourcePointCut() {
    }

    /**
     * 环绕通知
     *
     * @param point 切入点
     * @return 结果
     * @throws Throwable 异常
     */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        // 获取数据源注解
        DataSource dataSource = getDataSource(point);

        if (dataSource != null) {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceType(dataSource.value());
            log.debug("切换数据源为：{}", dataSource.value());
        }

        try {
            return point.proceed();
        } finally {
            // 恢复默认数据源
            DynamicDataSourceContextHolder.clearDataSourceType();
            log.debug("恢复默认数据源");
        }
    }

    /**
     * 获取数据源注解
     *
     * @param point 切入点
     * @return 数据源注解
     */
    private DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 优先获取方法上的注解
        DataSource dataSource = AnnotationUtils.findAnnotation(method, DataSource.class);
        if (dataSource != null) {
            return dataSource;
        }

        // 获取类上的注解
        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
}