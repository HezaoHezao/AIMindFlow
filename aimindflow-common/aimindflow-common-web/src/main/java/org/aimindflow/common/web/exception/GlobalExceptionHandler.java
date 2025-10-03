package org.aimindflow.common.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.core.exception.BaseException;
import org.aimindflow.common.core.exception.ServiceException;
import org.aimindflow.common.ratelimiter.exception.RateLimitException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

/**
 * 全局异常处理器
 *
 * 统一捕获服务端异常并返回标准响应结构 {@link R}。
 * 覆盖常见的业务异常、参数校验异常与未知异常。
 *
 * @author HezaoHezao
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常处理
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        Integer code = e.getCode();
        log.warn("业务异常: {}", e.getMessage());
        return code == null ? R.fail(e.getMessage()) : R.fail(code, e.getMessage());
    }

    /**
     * 基础异常处理
     */
    @ExceptionHandler(BaseException.class)
    public R<Void> handleBaseException(BaseException e) {
        log.warn("基础异常: {}", e.getDefaultMessage());
        String msg = e.getDefaultMessage();
        return R.fail(msg == null ? "操作失败" : msg);
    }

    /**
     * 限流异常处理
     */
    @ExceptionHandler(RateLimitException.class)
    public R<Void> handleRateLimitException(RateLimitException e) {
        log.warn("限流异常: {}", e.getMessage());
        return R.fail(e.getMessage());
    }

    /**
     * 参数绑定异常处理
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public R<Void> handleValidationException(Exception e) {
        String msg = "参数校验失败";
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            msg = be.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .findFirst().orElse(msg);
        } else if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
            msg = me.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .findFirst().orElse(msg);
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ce = (ConstraintViolationException) e;
            msg = ce.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ":" + v.getMessage())
                .findFirst().orElse(msg);
        }
        log.warn("参数异常: {}", msg);
        return R.fail(msg);
    }

    /**
     * 请求体解析异常处理
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("请求体解析异常: {}", e.getMessage());
        return R.fail("请求参数格式错误");
    }

    /**
     * 未知异常处理
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return R.fail("系统异常，请稍后再试");
    }
}