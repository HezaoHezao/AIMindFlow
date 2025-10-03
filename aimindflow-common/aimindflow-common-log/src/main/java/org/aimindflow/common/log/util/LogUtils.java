package org.aimindflow.common.log.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志工具类
 *
 * @author HezaoHezao
 */
@Slf4j
public class LogUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 获取堆栈信息
     *
     * @param throwable 异常信息
     * @return 堆栈信息
     */
    public static String getStackTraceAsString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint 连接点
     * @return 请求参数
     */
    public static String getRequestParams(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        // 构建参数键值对
        Map<String, Object> params = new HashMap<>(parameterNames.length);
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }

        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.error("转换请求参数失败", e);
            return "转换请求参数失败";
        }
    }

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object object) {
        if (object == null) {
            return "";
        }
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            return "对象转JSON字符串失败";
        }
    }

    /**
     * 获取客户端IP
     *
     * @param request 请求对象
     * @return 客户端IP
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取浏览器类型
     *
     * @param request 请求对象
     * @return 浏览器类型
     */
    public static String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return "未知浏览器";
        }
        if (userAgent.contains("MSIE")) {
            return "IE浏览器";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox浏览器";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome浏览器";
        } else if (userAgent.contains("Safari")) {
            return "Safari浏览器";
        } else if (userAgent.contains("Opera")) {
            return "Opera浏览器";
        } else {
            return "其他浏览器";
        }
    }

    /**
     * 获取操作系统类型
     *
     * @param request 请求对象
     * @return 操作系统类型
     */
    public static String getOs(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null || userAgent.isEmpty()) {
            return "未知操作系统";
        }
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "Mac OS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Unix")) {
            return "Unix";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iOS")) {
            return "iOS";
        } else {
            return "其他操作系统";
        }
    }

    /**
     * 获取主机名
     *
     * @return 主机名
     */
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.error("获取主机名失败", e);
            return "未知主机";
        }
    }

    /**
     * 获取主机IP
     *
     * @return 主机IP
     */
    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("获取主机IP失败", e);
            return "未知IP";
        }
    }

    /**
     * 获取应用名称
     *
     * @return 应用名称
     */
    public static String getApplicationName() {
        // 可以通过Spring Environment获取
        return "AIMindFlow";
    }

    /**
     * 获取当前用户ID
     *
     * @return 当前用户ID
     */
    public static String getCurrentUserId() {
        // 可以通过Spring Security或自定义认证获取
        return "未知用户ID";
    }

    /**
     * 获取当前用户名
     *
     * @return 当前用户名
     */
    public static String getCurrentUsername() {
        // 可以通过Spring Security或自定义认证获取
        return "未知用户";
    }

    /**
     * 获取当前租户ID
     *
     * @return 当前租户ID
     */
    public static String getCurrentTenantId() {
        // 可以通过自定义租户上下文获取
        return "未知租户ID";
    }

    /**
     * 获取当前请求对象
     *
     * @return 当前请求对象
     */
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            return attributes.getRequest();
        }
        return null;
    }
}