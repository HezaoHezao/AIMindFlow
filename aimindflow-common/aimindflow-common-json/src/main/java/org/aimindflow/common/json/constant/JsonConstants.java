package org.aimindflow.common.json.constant;

/**
 * JSON常量
 *
 * @author HezaoHezao
 */
public class JsonConstants {

    /**
     * JSON类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /**
     * JSON类型（UTF-8）
     */
    public static final String CONTENT_TYPE_JSON_UTF8 = "application/json;charset=UTF-8";

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 错误码：JSON解析错误
     */
    public static final int ERROR_CODE_JSON_PARSE = 10001;

    /**
     * 错误消息：JSON解析错误
     */
    public static final String ERROR_MESSAGE_JSON_PARSE = "JSON解析错误";

    /**
     * 错误码：JSON转换错误
     */
    public static final int ERROR_CODE_JSON_CONVERT = 10002;

    /**
     * 错误消息：JSON转换错误
     */
    public static final String ERROR_MESSAGE_JSON_CONVERT = "JSON转换错误";

    /**
     * 错误码：无效的JSON
     */
    public static final int ERROR_CODE_INVALID_JSON = 10003;

    /**
     * 错误消息：无效的JSON
     */
    public static final String ERROR_MESSAGE_INVALID_JSON = "无效的JSON";

    /**
     * 错误码：JSON序列化错误
     */
    public static final int ERROR_CODE_JSON_SERIALIZE = 10004;

    /**
     * 错误消息：JSON序列化错误
     */
    public static final String ERROR_MESSAGE_JSON_SERIALIZE = "JSON序列化错误";

    /**
     * 错误码：JSON反序列化错误
     */
    public static final int ERROR_CODE_JSON_DESERIALIZE = 10005;

    /**
     * 错误消息：JSON反序列化错误
     */
    public static final String ERROR_MESSAGE_JSON_DESERIALIZE = "JSON反序列化错误";
}