package org.aimindflow.common.mail.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 邮件信息实体类
 *
 * @author HezaoHezao
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 邮件ID
     */
    private String mailId;

    /**
     * 发件人
     */
    private String from;

    /**
     * 收件人（多个收件人用逗号分隔）
     */
    private String to;

    /**
     * 抄送人（多个抄送人用逗号分隔）
     */
    private String cc;

    /**
     * 密送人（多个密送人用逗号分隔）
     */
    private String bcc;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件类型（text/html）
     */
    private String contentType;

    /**
     * 附件路径列表
     */
    private List<String> attachmentPaths;

    /**
     * 附件列表
     */
    private File[] attachments;

    /**
     * 内联资源路径
     */
    private String inlineResourcePath;

    /**
     * 内联资源ID
     */
    private String inlineResourceId;

    /**
     * 模板名称
     */
    private String template;

    /**
     * 模板引擎类型（thymeleaf/freemarker）
     */
    private String templateEngine;

    /**
     * 模板参数
     */
    private Map<String, Object> templateModel;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 发送状态（0:未发送，1:发送成功，2:发送失败）
     */
    private Integer status;

    /**
     * 错误信息
     */
    private String errorMsg;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 最大重试次数
     */
    private Integer maxRetryCount;

    /**
     * 优先级（1:高，2:中，3:低）
     */
    private Integer priority;

    /**
     * 是否需要回执
     */
    private Boolean needReceipt;

    /**
     * 扩展字段
     */
    private Map<String, Object> ext;
}