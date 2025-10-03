package org.aimindflow.common.log.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.log.constant.LogConstants;
import org.aimindflow.common.log.entity.LogInfo;
import org.aimindflow.common.log.service.LogService;
import org.aimindflow.common.log.util.LogUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * 默认日志服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class DefaultLogServiceImpl implements LogService {

    @Override
    public void log(LogInfo logInfo) {
        // 设置默认值
        if (logInfo.getId() == null) {
            logInfo.setId(UUID.randomUUID().toString());
        }
        if (logInfo.getOperationTime() == null) {
            logInfo.setOperationTime(new Date());
        }
        if (logInfo.getLogLevel() == null) {
            logInfo.setLogLevel(LogConstants.DEFAULT_LOG_LEVEL);
        }

        // 根据日志级别记录日志
        switch (logInfo.getLogLevel()) {
            case LogConstants.LOG_LEVEL_TRACE:
                if (log.isTraceEnabled()) {
                    log.trace("[{}] {} - {}", logInfo.getModule(), logInfo.getOperationType(), logInfo.getDescription());
                }
                break;
            case LogConstants.LOG_LEVEL_DEBUG:
                if (log.isDebugEnabled()) {
                    log.debug("[{}] {} - {}", logInfo.getModule(), logInfo.getOperationType(), logInfo.getDescription());
                }
                break;
            case LogConstants.LOG_LEVEL_WARN:
                log.warn("[{}] {} - {}", logInfo.getModule(), logInfo.getOperationType(), logInfo.getDescription());
                break;
            case LogConstants.LOG_LEVEL_ERROR:
                log.error("[{}] {} - {}", logInfo.getModule(), logInfo.getOperationType(), logInfo.getDescription());
                break;
            case LogConstants.LOG_LEVEL_INFO:
            default:
                log.info("[{}] {} - {}", logInfo.getModule(), logInfo.getOperationType(), logInfo.getDescription());
                break;
        }

        // 这里可以扩展其他日志记录方式，如数据库、文件、消息队列等
    }

    @Override
    public void log(String module, String operationType, String description, String status) {
        LogInfo logInfo = new LogInfo()
                .setModule(module)
                .setOperationType(operationType)
                .setDescription(description)
                .setStatus(status)
                .setLogLevel(LogConstants.LOG_LEVEL_INFO);

        log(logInfo);
    }

    @Override
    public void logSuccess(String module, String operationType, String description) {
        log(module, operationType, description, LogConstants.OPERATION_STATUS_SUCCESS);
    }

    @Override
    public void logFail(String module, String operationType, String description, Throwable e) {
        LogInfo logInfo = new LogInfo()
                .setModule(module)
                .setOperationType(operationType)
                .setDescription(description)
                .setStatus(LogConstants.OPERATION_STATUS_FAIL)
                .setLogLevel(LogConstants.LOG_LEVEL_ERROR);

        if (e != null) {
            logInfo.setExceptionInfo(LogUtils.getStackTraceAsString(e));
            log.error("[{}] {} - {}", module, operationType, description, e);
        } else {
            log(logInfo);
        }
    }

    @Override
    public void logException(Throwable e) {
        logException(LogConstants.DEFAULT_MODULE, LogConstants.DEFAULT_DESCRIPTION, e);
    }

    @Override
    public void logException(String module, String description, Throwable e) {
        LogInfo logInfo = new LogInfo()
                .setModule(module)
                .setOperationType(LogConstants.OPERATION_TYPE_OTHER)
                .setDescription(description)
                .setStatus(LogConstants.OPERATION_STATUS_FAIL)
                .setLogLevel(LogConstants.LOG_LEVEL_ERROR);

        if (e != null) {
            logInfo.setExceptionInfo(LogUtils.getStackTraceAsString(e));
            log.error("[{}] {} - {}", module, LogConstants.OPERATION_TYPE_OTHER, description, e);
        } else {
            log(logInfo);
        }
    }

    @Override
    public void logLogin(String username, String status, String message) {
        LogInfo logInfo = new LogInfo()
                .setModule("认证模块")
                .setOperationType(LogConstants.OPERATION_TYPE_LOGIN)
                .setDescription(message)
                .setOperatorName(username)
                .setStatus(status)
                .setLogLevel(LogConstants.LOG_LEVEL_INFO);

        log(logInfo);
    }

    @Override
    public void logLogout(String username) {
        LogInfo logInfo = new LogInfo()
                .setModule("认证模块")
                .setOperationType(LogConstants.OPERATION_TYPE_LOGOUT)
                .setDescription("用户登出")
                .setOperatorName(username)
                .setStatus(LogConstants.OPERATION_STATUS_SUCCESS)
                .setLogLevel(LogConstants.LOG_LEVEL_INFO);

        log(logInfo);
    }
}