package org.aimindflow.common.chat.config;

import cn.hutool.core.util.StrUtil;
import org.aimindflow.common.chat.factory.ChatServiceFactory;
import org.aimindflow.common.chat.service.ChatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 聊天模块自动配置
 *
 * 绑定聊天配置，按需提供 ChatService 默认实现。
 */
@Configuration
@EnableConfigurationProperties(ChatConfig.class)
@ConditionalOnProperty(prefix = "aimindflow.chat", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ChatAutoConfiguration {

    /**
     * 当配置了 apiKey 时，提供默认的 OpenAI 聊天服务实现。
     */
    @Bean
    @ConditionalOnMissingBean(ChatService.class)
    public ChatService chatService(ChatConfig chatConfig) {
        if (StrUtil.isBlank(chatConfig.getApiKey())) {
            return null;
        }
        return ChatServiceFactory.createOpenAiChatService(chatConfig);
    }
}