package org.aimindflow.common.chat.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.BufferedSource;
import org.aimindflow.common.chat.openai.OpenAiAuthInterceptor;
import org.aimindflow.common.chat.request.ChatRequest;
import org.aimindflow.common.chat.response.ChatResponse;
import org.aimindflow.common.chat.service.ChatService;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * OpenAI聊天服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class OpenAiChatServiceImpl implements ChatService {

    /**
     * OpenAI API URL
     */
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * HTTP客户端
     */
    private final OkHttpClient client;

    /**
     * JSON对象映射器
     */
    private final ObjectMapper objectMapper;

    /**
     * 构造函数
     *
     * @param authInterceptor 认证拦截器
     */
    public OpenAiChatServiceImpl(OpenAiAuthInterceptor authInterceptor) {
        this.client = new OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 发送聊天请求
     *
     * @param chatRequest 聊天请求对象
     * @return 聊天响应对象
     */
    @Override
    public ChatResponse sendChatRequest(ChatRequest chatRequest) {
        try {
            // 设置非流式响应
            chatRequest.setStream(false);
            
            // 构建请求体
            String requestBody = objectMapper.writeValueAsString(chatRequest);
            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
            
            // 构建请求
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();
            
            // 发送请求
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }
                
                // 解析响应
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, ChatResponse.class);
            }
        } catch (Exception e) {
            log.error("Failed to send chat request", e);
            throw new RuntimeException("Failed to send chat request", e);
        }
    }

    /**
     * 发送流式聊天请求
     *
     * @param chatRequest 聊天请求对象
     * @param callback    回调函数
     */
    @Override
    public void sendStreamChatRequest(ChatRequest chatRequest, StreamChatCallback callback) {
        try {
            // 设置流式响应
            chatRequest.setStream(true);
            
            // 构建请求体
            String requestBody = objectMapper.writeValueAsString(chatRequest);
            RequestBody body = RequestBody.create(requestBody, MediaType.parse("application/json"));
            
            // 构建请求
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();
            
            // 发送异步请求
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        callback.onError(new IOException("Unexpected code " + response));
                        return;
                    }
                    
                    try (ResponseBody responseBody = response.body()) {
                        if (responseBody == null) {
                            callback.onError(new IOException("Response body is null"));
                            return;
                        }
                        
                        // 处理SSE流
                        try (BufferedSource source = responseBody.source()) {
                            while (!source.exhausted()) {
                                String line = source.readUtf8Line();
                                if (line == null) {
                                    break;
                                }
                                
                                if (line.startsWith("data: ")) {
                                    String data = line.substring(6);
                                    if ("[DONE]".equals(data)) {
                                        callback.onComplete();
                                        break;
                                    }
                                    
                                    try {
                                        ChatResponse chatResponse = objectMapper.readValue(data, ChatResponse.class);
                                        callback.onResponse(chatResponse);
                                    } catch (Exception e) {
                                        log.error("Failed to parse SSE data: {}", data, e);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        callback.onError(e);
                    } finally {
                        callback.onComplete();
                    }
                }
            });
        } catch (Exception e) {
            log.error("Failed to send stream chat request", e);
            callback.onError(e);
        }
    }
}