package org.aimindflow.common.chat.openai;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认的OpenAI认证拦截器实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class DefaultOpenAiAuthInterceptor implements OpenAiAuthInterceptor {

    /**
     * API密钥
     */
    private final String apiKey;

    /**
     * 构造函数
     *
     * @param apiKey API密钥
     */
    public DefaultOpenAiAuthInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * 获取API密钥
     *
     * @return API密钥
     */
    @Override
    public String getApiKey() {
        return this.apiKey;
    }
}