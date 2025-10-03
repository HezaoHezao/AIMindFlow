package org.aimindflow.common.chat.constant;

/**
 * 聊天错误码常量类
 *
 * @author HezaoHezao
 */
public class ChatErrorCode {

    /**
     * 通用错误码前缀
     */
    private static final String PREFIX = "CHAT_";

    /**
     * 系统错误
     */
    public static final String SYSTEM_ERROR = PREFIX + "SYSTEM_ERROR";

    /**
     * 参数错误
     */
    public static final String PARAM_ERROR = PREFIX + "PARAM_ERROR";

    /**
     * API密钥错误
     */
    public static final String API_KEY_ERROR = PREFIX + "API_KEY_ERROR";

    /**
     * 网络错误
     */
    public static final String NETWORK_ERROR = PREFIX + "NETWORK_ERROR";

    /**
     * 超时错误
     */
    public static final String TIMEOUT_ERROR = PREFIX + "TIMEOUT_ERROR";

    /**
     * 模型不存在
     */
    public static final String MODEL_NOT_EXIST = PREFIX + "MODEL_NOT_EXIST";

    /**
     * 内容审核失败
     */
    public static final String CONTENT_FILTER = PREFIX + "CONTENT_FILTER";

    /**
     * 请求过于频繁
     */
    public static final String RATE_LIMIT = PREFIX + "RATE_LIMIT";

    /**
     * 配额不足
     */
    public static final String QUOTA_EXCEEDED = PREFIX + "QUOTA_EXCEEDED";

    /**
     * 服务不可用
     */
    public static final String SERVICE_UNAVAILABLE = PREFIX + "SERVICE_UNAVAILABLE";

    /**
     * 未知错误
     */
    public static final String UNKNOWN_ERROR = PREFIX + "UNKNOWN_ERROR";
}