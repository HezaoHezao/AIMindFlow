package org.aimindflow.common.mail.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * 邮件异常类
 *
 * @author HezaoHezao
 */
public class MailException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     */
    public MailException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public MailException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause   异常原因
     */
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public MailException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public MailException(String code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param cause   异常原因
     */
    public MailException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}