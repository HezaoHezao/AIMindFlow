package org.aimindflow.common.doc.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * 文档处理异常类
 *
 * @author HezaoHezao
 */
public class DocException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造一个没有详细错误信息的异常
     */
    public DocException() {
        super();
    }

    /**
     * 构造一个带有详细错误信息的异常
     *
     * @param message 详细错误信息
     */
    public DocException(String message) {
        super(message);
    }

    /**
     * 构造一个带有详细错误信息和原因的异常
     *
     * @param message 详细错误信息
     * @param cause   原因
     */
    public DocException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有原因的异常
     *
     * @param cause 原因
     */
    public DocException(Throwable cause) {
        super(cause);
    }
}