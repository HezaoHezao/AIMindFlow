package org.aimindflow.common.log.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * 日志异常类
 *
 * @author HezaoHezao
 */
public class LogException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public LogException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public LogException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause   异常原因
     */
    public LogException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public LogException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public LogException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   异常原因
     */
    public LogException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}