package org.aimindflow.common.mail.constant;

/**
 * 邮件常量类
 *
 * @author HezaoHezao
 */
public class MailConstants {

    /**
     * 邮件类型 - 文本
     */
    public static final String MAIL_TYPE_TEXT = "text/plain;charset=UTF-8";

    /**
     * 邮件类型 - HTML
     */
    public static final String MAIL_TYPE_HTML = "text/html;charset=UTF-8";

    /**
     * 模板引擎 - Thymeleaf
     */
    public static final String TEMPLATE_ENGINE_THYMELEAF = "thymeleaf";

    /**
     * 模板引擎 - Freemarker
     */
    public static final String TEMPLATE_ENGINE_FREEMARKER = "freemarker";

    /**
     * 发送状态 - 未发送
     */
    public static final int SEND_STATUS_UNSENT = 0;

    /**
     * 发送状态 - 发送成功
     */
    public static final int SEND_STATUS_SUCCESS = 1;

    /**
     * 发送状态 - 发送失败
     */
    public static final int SEND_STATUS_FAIL = 2;

    /**
     * 优先级 - 高
     */
    public static final int PRIORITY_HIGH = 1;

    /**
     * 优先级 - 中
     */
    public static final int PRIORITY_MEDIUM = 2;

    /**
     * 优先级 - 低
     */
    public static final int PRIORITY_LOW = 3;

    /**
     * 默认最大重试次数
     */
    public static final int DEFAULT_MAX_RETRY_COUNT = 3;

    /**
     * 默认模板路径
     */
    public static final String DEFAULT_TEMPLATE_PATH = "classpath:/templates/mail/";

    /**
     * 默认模板后缀 - Thymeleaf
     */
    public static final String DEFAULT_TEMPLATE_SUFFIX_THYMELEAF = ".html";

    /**
     * 默认模板后缀 - Freemarker
     */
    public static final String DEFAULT_TEMPLATE_SUFFIX_FREEMARKER = ".ftl";

    /**
     * 错误码 - 邮件发送失败
     */
    public static final String ERROR_CODE_SEND_FAIL = "MAIL-10001";

    /**
     * 错误消息 - 邮件发送失败
     */
    public static final String ERROR_MESSAGE_SEND_FAIL = "邮件发送失败";

    /**
     * 错误码 - 邮件模板不存在
     */
    public static final String ERROR_CODE_TEMPLATE_NOT_FOUND = "MAIL-10002";

    /**
     * 错误消息 - 邮件模板不存在
     */
    public static final String ERROR_MESSAGE_TEMPLATE_NOT_FOUND = "邮件模板不存在";

    /**
     * 错误码 - 邮件参数错误
     */
    public static final String ERROR_CODE_PARAM_ERROR = "MAIL-10003";

    /**
     * 错误消息 - 邮件参数错误
     */
    public static final String ERROR_MESSAGE_PARAM_ERROR = "邮件参数错误";
}