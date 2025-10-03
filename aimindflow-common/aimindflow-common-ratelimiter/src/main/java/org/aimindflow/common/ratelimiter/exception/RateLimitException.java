package org.aimindflow.common.ratelimiter.exception;

/**
 * 限流异常
 *
 * @author HezaoHezao
 */
public class RateLimitException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RateLimitException() {
        super("访问过于频繁，请稍后再试");
    }

    public RateLimitException(String message) {
        super(message);
    }

    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}