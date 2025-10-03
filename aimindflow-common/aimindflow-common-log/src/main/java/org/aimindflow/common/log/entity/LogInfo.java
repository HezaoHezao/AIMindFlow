package org.aimindflow.common.log.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志信息实体
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    private String id;

    /**
     * 模块名称
     */
    private String module;

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 操作人ID
     */
    private String operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作人IP
     */
    private String operatorIp;

    /**
     * 操作人浏览器
     */
    private String operatorBrowser;

    /**
     * 操作人操作系统
     */
    private String operatorOs;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应结果
     */
    private String responseResult;

    /**
     * 异常信息
     */
    private String exceptionInfo;

    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;

    /**
     * 操作状态（成功/失败）
     */
    private String status;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 日志级别
     */
    private String logLevel;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 线程ID
     */
    private String threadId;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 主机名
     */
    private String hostName;

    /**
     * 主机IP
     */
    private String hostIp;

    /**
     * 扩展字段1
     */
    private String ext1;

    /**
     * 扩展字段2
     */
    private String ext2;

    /**
     * 扩展字段3
     */
    private String ext3;
}