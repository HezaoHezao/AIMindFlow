package org.aimindflow.common.mybatis.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * MyBatis异常类
 *
 * @author HezaoHezao
 */
public class MybatisException extends BaseException {

    private static final long serialVersionUID = 1L;

    public MybatisException(String message) {
        super(message);
    }

    public MybatisException(String message, Throwable cause) {
        super(message, cause);
    }

    public MybatisException(Throwable cause) {
        super(cause);
    }

    public MybatisException(String code, String message) {
        super(code, message);
    }

    public MybatisException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}