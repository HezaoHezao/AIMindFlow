package org.aimindflow.common.chat.service;

import org.aimindflow.common.chat.request.ChatRequest;
import org.aimindflow.common.chat.response.ChatResponse;

/**
 * 聊天服务接口
 *
 * @author HezaoHezao
 */
public interface ChatService {

    /**
     * 发送聊天请求
     *
     * @param chatRequest 聊天请求对象
     * @return 聊天响应对象
     */
    ChatResponse sendChatRequest(ChatRequest chatRequest);

    /**
     * 发送流式聊天请求
     *
     * @param chatRequest 聊天请求对象
     * @param callback    回调函数
     */
    void sendStreamChatRequest(ChatRequest chatRequest, StreamChatCallback callback);

    /**
     * 流式聊天回调接口
     */
    interface StreamChatCallback {
        /**
         * 处理聊天响应
         *
         * @param response 聊天响应对象
         */
        void onResponse(ChatResponse response);

        /**
         * 处理错误
         *
         * @param throwable 异常对象
         */
        void onError(Throwable throwable);

        /**
         * 处理完成事件
         */
        void onComplete();
    }
}