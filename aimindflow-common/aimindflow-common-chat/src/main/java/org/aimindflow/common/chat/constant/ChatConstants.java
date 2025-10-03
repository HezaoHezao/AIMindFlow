package org.aimindflow.common.chat.constant;

/**
 * 聊天常量类
 *
 * @author HezaoHezao
 */
public class ChatConstants {

    /**
     * 角色：系统
     */
    public static final String ROLE_SYSTEM = "system";

    /**
     * 角色：用户
     */
    public static final String ROLE_USER = "user";

    /**
     * 角色：助手
     */
    public static final String ROLE_ASSISTANT = "assistant";

    /**
     * 角色：函数
     */
    public static final String ROLE_FUNCTION = "function";

    /**
     * 模型：GPT-3.5-Turbo
     */
    public static final String MODEL_GPT_3_5_TURBO = "gpt-3.5-turbo";

    /**
     * 模型：GPT-4
     */
    public static final String MODEL_GPT_4 = "gpt-4";

    /**
     * 模型：GPT-4-Turbo
     */
    public static final String MODEL_GPT_4_TURBO = "gpt-4-turbo";

    /**
     * 结束原因：停止
     */
    public static final String FINISH_REASON_STOP = "stop";

    /**
     * 结束原因：长度
     */
    public static final String FINISH_REASON_LENGTH = "length";

    /**
     * 结束原因：内容过滤
     */
    public static final String FINISH_REASON_CONTENT_FILTER = "content_filter";

    /**
     * 结束原因：函数调用
     */
    public static final String FINISH_REASON_FUNCTION_CALL = "function_call";

    /**
     * 结束原因：空
     */
    public static final String FINISH_REASON_NULL = "null";

    /**
     * OpenAI API URL
     */
    public static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    /**
     * 流式响应结束标记
     */
    public static final String STREAM_DONE_MARKER = "[DONE]";

    /**
     * 流式响应数据前缀
     */
    public static final String STREAM_DATA_PREFIX = "data: ";
}