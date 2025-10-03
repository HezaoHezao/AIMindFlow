package org.aimindflow.common.oss.exception;

import org.aimindflow.common.core.exception.BaseException;

/**
 * OSS异常类
 *
 * @author HezaoHezao
 */
public class OssException extends BaseException {

    private static final long serialVersionUID = 1L;

    public OssException(String message) {
        super(message);
    }

    public OssException(String message, Throwable cause) {
        super(message, cause);
    }

    public OssException(Throwable cause) {
        super(cause);
    }

    public OssException(String code, String message) {
        super(code, message);
    }

    public OssException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}