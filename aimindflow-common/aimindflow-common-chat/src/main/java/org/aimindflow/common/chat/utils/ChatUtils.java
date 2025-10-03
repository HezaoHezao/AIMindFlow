package org.aimindflow.common.chat.utils;

import org.aimindflow.common.chat.constant.ChatConstants;
import org.aimindflow.common.chat.request.ChatRequest;
import org.aimindflow.common.chat.response.ChatResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天工具类
 *
 * @author HezaoHezao
 */
public class ChatUtils {

    /**
     * 创建一个简单的聊天请求
     *
     * @param model   模型名称
     * @param content 用户消息内容
     * @return 聊天请求对象
     */
    public static ChatRequest createSimpleChatRequest(String model, String content) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);
        
        List<ChatRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatRequest.Message(ChatConstants.ROLE_USER, content));
        chatRequest.setMessages(messages);
        
        return chatRequest;
    }

    /**
     * 创建一个带系统提示的聊天请求
     *
     * @param model          模型名称
     * @param systemPrompt   系统提示内容
     * @param userContent    用户消息内容
     * @return 聊天请求对象
     */
    public static ChatRequest createChatRequestWithSystemPrompt(String model, String systemPrompt, String userContent) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);
        
        List<ChatRequest.Message> messages = new ArrayList<>();
        messages.add(new ChatRequest.Message(ChatConstants.ROLE_SYSTEM, systemPrompt));
        messages.add(new ChatRequest.Message(ChatConstants.ROLE_USER, userContent));
        chatRequest.setMessages(messages);
        
        return chatRequest;
    }

    /**
     * 创建一个带历史消息的聊天请求
     *
     * @param model       模型名称
     * @param messages    历史消息列表
     * @param userContent 新的用户消息内容
     * @return 聊天请求对象
     */
    public static ChatRequest createChatRequestWithHistory(String model, List<ChatRequest.Message> messages, String userContent) {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(model);
        
        List<ChatRequest.Message> newMessages = new ArrayList<>(messages);
        newMessages.add(new ChatRequest.Message(ChatConstants.ROLE_USER, userContent));
        chatRequest.setMessages(newMessages);
        
        return chatRequest;
    }

    /**
     * 从聊天响应中提取文本内容
     *
     * @param chatResponse 聊天响应对象
     * @return 文本内容
     */
    public static String extractContentFromResponse(ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return "";
        }
        
        ChatResponse.Choice choice = chatResponse.getChoices().get(0);
        if (choice == null || choice.getMessage() == null) {
            return "";
        }
        
        return choice.getMessage().getContent();
    }

    /**
     * 判断聊天响应是否成功
     *
     * @param chatResponse 聊天响应对象
     * @return 是否成功
     */
    public static boolean isResponseSuccessful(ChatResponse chatResponse) {
        return chatResponse != null && 
               chatResponse.getChoices() != null && 
               !chatResponse.getChoices().isEmpty() && 
               chatResponse.getChoices().get(0) != null && 
               chatResponse.getChoices().get(0).getMessage() != null && 
               chatResponse.getChoices().get(0).getMessage().getContent() != null;
    }

    /**
     * 获取聊天响应的结束原因
     *
     * @param chatResponse 聊天响应对象
     * @return 结束原因
     */
    public static String getFinishReason(ChatResponse chatResponse) {
        if (chatResponse == null || chatResponse.getChoices() == null || chatResponse.getChoices().isEmpty()) {
            return ChatConstants.FINISH_REASON_NULL;
        }
        
        ChatResponse.Choice choice = chatResponse.getChoices().get(0);
        if (choice == null) {
            return ChatConstants.FINISH_REASON_NULL;
        }
        
        return choice.getFinishReason();
    }
}