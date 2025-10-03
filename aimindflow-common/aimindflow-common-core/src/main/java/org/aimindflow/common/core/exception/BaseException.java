package org.aimindflow.common.core.exception;

/**
 * 基础异常
 * 
 * @author HezaoHezao
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误码对应的参数
     */
    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public BaseException(String module, String code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }

    // 移除旧的易与新重载冲突的构造器

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    /**
     * 无参构造
     */
    public BaseException() {
        super();
    }

    /**
     * 仅包含原因的构造
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    /**
     * 错误消息 + 原因
     */
    public BaseException(String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
        this.defaultMessage = defaultMessage;
    }

    /**
     * 错误消息 + 参数
     */
    public BaseException(String defaultMessage, Object... args) {
        this(null, null, args, defaultMessage);
    }

    /**
     * 错误码 + 错误消息
     */
    public BaseException(String code, String defaultMessage) {
        this(null, code, null, defaultMessage);
    }

    /**
     * 错误码 + 错误消息 + 原因
     */
    public BaseException(String code, String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    /**
     * 错误码 + 错误消息 + 参数
     */
    public BaseException(String code, String defaultMessage, Object... args) {
        this(null, code, args, defaultMessage);
    }

    /**
     * Integer 错误码 + 错误消息
     */
    public BaseException(Integer code, String defaultMessage) {
        this(String.valueOf(code), defaultMessage);
    }

    /**
     * Integer 错误码 + 错误消息 + 原因
     */
    public BaseException(Integer code, String defaultMessage, Throwable cause) {
        this(String.valueOf(code), defaultMessage, cause);
    }

    /**
     * Integer 错误码 + 错误消息 + 参数
     */
    public BaseException(Integer code, String defaultMessage, Object... args) {
        this(String.valueOf(code), defaultMessage, args);
    }

    public String getModule() {
        return module;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}