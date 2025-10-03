package org.aimindflow.common.translation.handler;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.translation.properties.TranslationProperties;
import org.aimindflow.common.translation.util.TranslationUtils;
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
 * 统一响应翻译处理
 *
 * 在响应返回前，对对象中被 @Translation 标注的字段进行翻译处理。
 * 支持对 R<T> 的 data 进行递归处理，也支持直接返回对象的翻译。
 *
 * @author HezaoHezao
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnProperty(prefix = "translation", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TranslationResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private TranslationProperties properties;

    @Autowired
    private TranslationUtils translationUtils;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return properties.isEnabled();
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        try {
            if (body == null) {
                return null;
            }
            if (body instanceof R) {
                R<?> r = (R<?>) body;
                Object data = r.getData();
                translationUtils.translateObject(data);
                return r;
            }
            translationUtils.translateObject(body);
            return body;
        } catch (Exception e) {
            log.warn("响应翻译处理异常", e);
            return body;
        }
    }
}