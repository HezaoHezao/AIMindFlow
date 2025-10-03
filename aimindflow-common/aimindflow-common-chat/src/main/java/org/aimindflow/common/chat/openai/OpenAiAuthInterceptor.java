package org.aimindflow.common.chat.openai;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * OpenAI认证拦截器接口
 *
 * @author HezaoHezao
 */
public interface OpenAiAuthInterceptor extends Interceptor {

    /**
     * 获取API密钥
     *
     * @return API密钥
     */
    String getApiKey();

    /**
     * 拦截请求，添加认证信息
     *
     * @param chain 拦截器链
     * @return 响应
     * @throws IOException IO异常
     */
    @Override
    default Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
                .header("Authorization", "Bearer " + getApiKey())
                .header("Content-Type", "application/json")
                .method(original.method(), original.body())
                .build();
        return chain.proceed(request);
    }
}