package org.aimindflow.common.idempotent.exception;

import org.aimindflow.common.core.exception.BaseException;
import org.aimindflow.common.idempotent.constant.IdempotentConstants;

/**
 * 幂等异常类
 *
 * @author HezaoHezao
 */
public class IdempotentException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public IdempotentException(String message) {
        super(IdempotentConstants.ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param args    参数
     */
    public IdempotentException(String message, Object... args) {
        super(IdempotentConstants.ERROR_CODE, message, args);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param e       异常
     */
    public IdempotentException(String message, Throwable e) {
        super(IdempotentConstants.ERROR_CODE, message, e);
    }

    /**
     * 构造函数
     *
     * @param e 异常
     */
    public IdempotentException(Throwable e) {
        super(e);
    }
}