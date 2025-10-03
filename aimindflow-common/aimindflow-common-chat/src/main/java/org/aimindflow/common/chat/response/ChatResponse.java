package org.aimindflow.common.chat.response;

import lombok.Data;

import java.util.List;

/**
 * 聊天响应对象
 *
 * @author HezaoHezao
 */
@Data
public class ChatResponse {

    /**
     * 响应ID
     */
    private String id;

    /**
     * 对象类型
     */
    private String object;

    /**
     * 创建时间
     */
    private Long created;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 系统指纹
     */
    private String systemFingerprint;

    /**
     * 选择列表
     */
    private List<Choice> choices;

    /**
     * 使用情况
     */
    private Usage usage;

    /**
     * 选择对象
     */
    @Data
    public static class Choice {
        /**
         * 索引
         */
        private Integer index;

        /**
         * 消息
         */
        private Message message;

        /**
         * 结束原因
         */
        private String finishReason;
    }

    /**
     * 消息对象
     */
    @Data
    public static class Message {
        /**
         * 角色
         */
        private String role;

        /**
         * 内容
         */
        private String content;
    }

    /**
     * 使用情况对象
     */
    @Data
    public static class Usage {
        /**
         * 提示令牌数
         */
        private Integer promptTokens;

        /**
         * 完成令牌数
         */
        private Integer completionTokens;

        /**
         * 总令牌数
         */
        private Integer totalTokens;
    }
}