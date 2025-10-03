package org.aimindflow.common.log.service;

import org.aimindflow.common.log.entity.LogInfo;

/**
 * 日志服务接口
 *
 * @author HezaoHezao
 */
public interface LogService {

    /**
     * 记录日志
     *
     * @param logInfo 日志信息
     */
    void log(LogInfo logInfo);

    /**
     * 记录普通日志
     *
     * @param module        模块名称
     * @param operationType 操作类型
     * @param description   操作描述
     * @param status        操作状态
     */
    void log(String module, String operationType, String description, String status);

    /**
     * 记录成功日志
     *
     * @param module        模块名称
     * @param operationType 操作类型
     * @param description   操作描述
     */
    void logSuccess(String module, String operationType, String description);

    /**
     * 记录失败日志
     *
     * @param module        模块名称
     * @param operationType 操作类型
     * @param description   操作描述
     * @param e             异常信息
     */
    void logFail(String module, String operationType, String description, Throwable e);

    /**
     * 记录异常日志
     *
     * @param e 异常信息
     */
    void logException(Throwable e);

    /**
     * 记录异常日志
     *
     * @param module      模块名称
     * @param description 操作描述
     * @param e           异常信息
     */
    void logException(String module, String description, Throwable e);

    /**
     * 记录登录日志
     *
     * @param username 用户名
     * @param status   登录状态
     * @param message  登录消息
     */
    void logLogin(String username, String status, String message);

    /**
     * 记录登出日志
     *
     * @param username 用户名
     */
    void logLogout(String username);
}