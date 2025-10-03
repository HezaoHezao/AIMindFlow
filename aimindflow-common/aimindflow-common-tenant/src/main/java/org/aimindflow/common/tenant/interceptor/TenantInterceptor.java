package org.aimindflow.common.tenant.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.tenant.context.TenantContext;
import org.aimindflow.common.tenant.properties.TenantProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 租户拦截器
 * 从请求中提取租户ID并设置到上下文中
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class TenantInterceptor implements HandlerInterceptor {

    @Autowired
    private TenantProperties tenantProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果未启用多租户，直接通过
        if (!tenantProperties.isEnabled()) {
            return true;
        }

        // 从请求中获取租户ID
        String tenantId = extractTenantId(request);

        // 如果没有获取到租户ID，使用默认租户ID
        if (!StringUtils.hasText(tenantId)) {
            if (tenantProperties.isIgnoreTenantIdEmpty()) {
                tenantId = tenantProperties.getDefaultTenantId();
                log.debug("使用默认租户ID: {}", tenantId);
            } else {
                log.warn("请求中未找到租户ID，URI: {}", request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"code\":400,\"message\":\"租户ID不能为空\"}");
                return false;
            }
        }

        // 设置租户ID到上下文
        TenantContext.setTenantId(tenantId);
        log.debug("设置请求租户ID: {}, URI: {}", tenantId, request.getRequestURI());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除租户上下文
        TenantContext.clear();
        log.debug("清除租户上下文，URI: {}", request.getRequestURI());
    }

    /**
     * 从请求中提取租户ID
     *
     * @param request HTTP请求
     * @return 租户ID
     */
    private String extractTenantId(HttpServletRequest request) {
        String tenantId = null;

        // 1. 从请求头获取
        tenantId = request.getHeader(tenantProperties.getTenantIdHeader());
        if (StringUtils.hasText(tenantId)) {
            log.debug("从请求头获取租户ID: {}", tenantId);
            return tenantId;
        }

        // 2. 从请求参数获取
        tenantId = request.getParameter(tenantProperties.getTenantIdParam());
        if (StringUtils.hasText(tenantId)) {
            log.debug("从请求参数获取租户ID: {}", tenantId);
            return tenantId;
        }

        // 3. 从路径变量获取（如果URL包含租户ID）
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/tenant/")) {
            String[] pathParts = requestURI.split("/");
            if (pathParts.length > 2) {
                tenantId = pathParts[2];
                if (StringUtils.hasText(tenantId)) {
                    log.debug("从路径变量获取租户ID: {}", tenantId);
                    return tenantId;
                }
            }
        }

        log.debug("未从请求中获取到租户ID");
        return null;
    }
}