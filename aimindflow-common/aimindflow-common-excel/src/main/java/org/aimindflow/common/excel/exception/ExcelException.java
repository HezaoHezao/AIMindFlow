package org.aimindflow.common.excel.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * Excel操作异常类
 *
 * @author HezaoHezao
 */
public class ExcelException extends BaseException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public ExcelException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param args    参数
     */
    public ExcelException(String message, Object... args) {
        super(message, args);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param e       异常
     */
    public ExcelException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * 构造函数
     *
     * @param e 异常
     */
    public ExcelException(Throwable e) {
        super(e);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     */
    public ExcelException(Integer code, String message) {
        super(code, message);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param args    参数
     */
    public ExcelException(Integer code, String message, Object... args) {
        super(code, message, args);
    }

    /**
     * 构造函数
     *
     * @param code    错误码
     * @param message 错误消息
     * @param e       异常
     */
    public ExcelException(Integer code, String message, Throwable e) {
        super(code, message, e);
    }
}