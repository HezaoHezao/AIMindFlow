package org.aimindflow.common.sensitive.handler;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sensitive.properties.SensitiveProperties;
import org.aimindflow.common.sensitive.utils.SensitiveUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应体脱敏处理器
 * 自动对返回的响应体进行脱敏处理
 *
 * @author HezaoHezao
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "aimindflow.sensitive", name = "response-enabled", havingValue = "true", matchIfMissing = true)
public class SensitiveResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private SensitiveUtils sensitiveUtils;

    @Autowired
    private SensitiveProperties sensitiveProperties;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 只有启用了敏感数据处理才进行拦截
        return sensitiveProperties.getEnabled() && sensitiveProperties.getResponseEnabled();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            // 对响应体进行脱敏处理
            return sensitiveUtils.desensitize(body);
        } catch (Exception e) {
            log.error("响应体脱敏处理失败", e);
            return body;
        }
    }
}