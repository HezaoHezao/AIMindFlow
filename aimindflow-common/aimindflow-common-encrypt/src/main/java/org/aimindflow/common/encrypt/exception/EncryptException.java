package org.aimindflow.common.encrypt.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * 加密异常类
 *
 * @author HezaoHezao
 */
public class EncryptException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造一个没有详细错误信息的异常
     */
    public EncryptException() {
        super();
    }

    /**
     * 构造一个带有详细错误信息的异常
     *
     * @param message 详细错误信息
     */
    public EncryptException(String message) {
        super(message);
    }

    /**
     * 构造一个带有详细错误信息和原因的异常
     *
     * @param message 详细错误信息
     * @param cause   原因
     */
    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有原因的异常
     *
     * @param cause 原因
     */
    public EncryptException(Throwable cause) {
        super(cause);
    }
}