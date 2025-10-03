package org.aimindflow.common.log.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.log.annotation.Log;
import org.aimindflow.common.log.constant.LogConstants;
import org.aimindflow.common.log.entity.LogInfo;
import org.aimindflow.common.log.service.LogService;
import org.aimindflow.common.log.util.LogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

/**
 * 日志切面
 *
 * @author HezaoHezao
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    /**
     * 定义切点
     */
    @Pointcut("@annotation(org.aimindflow.common.log.annotation.Log)")
    public void logPointcut() {
    }

    /**
     * 环绕通知处理
     *
     * @param joinPoint 连接点
     * @return 方法执行结果
     * @throws Throwable 异常信息
     */
    @Around("logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();
        // 执行结果
        Object result = null;
        // 日志信息
        LogInfo logInfo = createLogInfo(joinPoint);

        try {
            // 执行方法
            result = joinPoint.proceed();
            // 设置操作状态为成功
            logInfo.setStatus(LogConstants.OPERATION_STATUS_SUCCESS);
            // 记录响应结果
            if (logInfo.getResponseResult() == null && result != null) {
                Log logAnnotation = getLogAnnotation(joinPoint);
                if (logAnnotation.recordResult()) {
                    logInfo.setResponseResult(LogUtils.toJsonString(result));
                }
            }
            return result;
        } catch (Throwable e) {
            // 设置操作状态为失败
            logInfo.setStatus(LogConstants.OPERATION_STATUS_FAIL);
            // 记录异常信息
            Log logAnnotation = getLogAnnotation(joinPoint);
            if (logAnnotation.recordException()) {
                logInfo.setExceptionInfo(LogUtils.getStackTraceAsString(e));
            }
            throw e;
        } finally {
            // 设置执行时间
            long executionTime = System.currentTimeMillis() - startTime;
            Log logAnnotation = getLogAnnotation(joinPoint);
            if (logAnnotation.recordExecutionTime()) {
                logInfo.setExecutionTime(executionTime);
            }
            // 记录日志
            logService.log(logInfo);
        }
    }

    /**
     * 创建日志信息
     *
     * @param joinPoint 连接点
     * @return 日志信息
     */
    private LogInfo createLogInfo(JoinPoint joinPoint) {
        // 获取注解信息
        Log logAnnotation = getLogAnnotation(joinPoint);

        // 创建日志信息
        LogInfo logInfo = new LogInfo();
        logInfo.setId(UUID.randomUUID().toString());
        logInfo.setModule(logAnnotation.module());
        logInfo.setOperationType(logAnnotation.operationType());
        logInfo.setDescription(logAnnotation.description());
        logInfo.setOperationTime(new Date());
        logInfo.setLogLevel(LogConstants.DEFAULT_LOG_LEVEL);

        // 获取类名和方法名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logInfo.setClassName(joinPoint.getTarget().getClass().getName());
        logInfo.setMethodName(signature.getName());

        // 获取线程信息
        logInfo.setThreadId(String.valueOf(Thread.currentThread().getId()));
        logInfo.setThreadName(Thread.currentThread().getName());

        // 获取主机信息
        logInfo.setHostName(LogUtils.getHostName());
        logInfo.setHostIp(LogUtils.getHostIp());

        // 获取应用名称
        logInfo.setApplicationName(LogUtils.getApplicationName());

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 设置请求信息
            logInfo.setRequestMethod(request.getMethod());
            logInfo.setRequestUrl(request.getRequestURI());

            // 记录请求参数
            if (logAnnotation.recordParams()) {
                logInfo.setRequestParams(LogUtils.getRequestParams(joinPoint));
            }

            // 记录操作人信息
            if (logAnnotation.recordOperator()) {
                // 获取操作人信息（可以根据实际情况获取）
                logInfo.setOperatorId(LogUtils.getCurrentUserId());
                logInfo.setOperatorName(LogUtils.getCurrentUsername());
                logInfo.setOperatorIp(LogUtils.getClientIp(request));
                logInfo.setOperatorBrowser(LogUtils.getBrowser(request));
                logInfo.setOperatorOs(LogUtils.getOs(request));
            }

            // 获取租户ID
            logInfo.setTenantId(LogUtils.getCurrentTenantId());
        }

        return logInfo;
    }

    /**
     * 获取日志注解
     *
     * @param joinPoint 连接点
     * @return 日志注解
     */
    private Log getLogAnnotation(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        return method.getAnnotation(Log.class);
    }
}