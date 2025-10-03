package org.aimindflow.common.chat.request;

import lombok.Data;

import java.util.List;

/**
 * 聊天请求对象
 *
 * @author HezaoHezao
 */
@Data
public class ChatRequest {

    /**
     * 模型名称
     */
    private String model;

    /**
     * 消息列表
     */
    private List<Message> messages;

    /**
     * 温度参数，控制输出的随机性
     */
    private Double temperature;

    /**
     * 最大生成的token数
     */
    private Integer maxTokens;

    /**
     * 是否流式响应
     */
    private Boolean stream;

    /**
     * 用户标识
     */
    private String user;

    /**
     * 消息对象
     */
    @Data
    public static class Message {
        /**
         * 角色：system, user, assistant
         */
        private String role;

        /**
         * 内容
         */
        private String content;

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}