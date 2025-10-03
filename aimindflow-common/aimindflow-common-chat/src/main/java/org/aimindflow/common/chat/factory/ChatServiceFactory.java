package org.aimindflow.common.chat.factory;

import org.aimindflow.common.chat.config.ChatConfig;
import org.aimindflow.common.chat.openai.DefaultOpenAiAuthInterceptor;
import org.aimindflow.common.chat.openai.OpenAiAuthInterceptor;
import org.aimindflow.common.chat.service.ChatService;
import org.aimindflow.common.chat.service.impl.OpenAiChatServiceImpl;

/**
 * 聊天服务工厂类
 *
 * @author HezaoHezao
 */
public class ChatServiceFactory {

    /**
     * 创建OpenAI聊天服务
     *
     * @param config 聊天配置
     * @return 聊天服务实例
     */
    public static ChatService createOpenAiChatService(ChatConfig config) {
        // 创建认证拦截器
        OpenAiAuthInterceptor authInterceptor = new DefaultOpenAiAuthInterceptor(config.getApiKey());
        
        // 创建聊天服务
        return new OpenAiChatServiceImpl(authInterceptor);
    }

    /**
     * 创建OpenAI聊天服务
     *
     * @param apiKey API密钥
     * @return 聊天服务实例
     */
    public static ChatService createOpenAiChatService(String apiKey) {
        ChatConfig config = new ChatConfig();
        config.setApiKey(apiKey);
        return createOpenAiChatService(config);
    }

    /**
     * 创建自定义认证的OpenAI聊天服务
     *
     * @param authInterceptor 认证拦截器
     * @return 聊天服务实例
     */
    public static ChatService createOpenAiChatService(OpenAiAuthInterceptor authInterceptor) {
        return new OpenAiChatServiceImpl(authInterceptor);
    }
}