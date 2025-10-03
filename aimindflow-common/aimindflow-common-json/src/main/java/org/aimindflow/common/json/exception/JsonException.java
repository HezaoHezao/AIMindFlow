package org.aimindflow.common.json.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * JSON异常
 *
 * @author HezaoHezao
 */
public class JsonException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public JsonException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public JsonException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause   异常原因
     */
    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public JsonException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public JsonException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   异常原因
     */
    public JsonException(Integer code, String message, Throwable cause) {
        super(code, message, cause);
    }
}