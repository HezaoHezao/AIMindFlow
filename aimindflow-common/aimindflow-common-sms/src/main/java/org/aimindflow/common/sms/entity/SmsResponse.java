package org.aimindflow.common.sms.entity;

import lombok.Data;

/**
 * 短信发送响应实体
 *
 * @author HezaoHezao
 */
@Data
public class SmsResponse {

    /**
     * 是否发送成功
     */
    private Boolean success;

    /**
     * 响应码
     */
    private String code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 发送回执ID
     */
    private String bizId;

    /**
     * 构造函数
     */
    public SmsResponse() {
    }

    /**
     * 构造函数
     *
     * @param success 是否成功
     * @param message 消息
     */
    public SmsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param success   是否成功
     * @param code      响应码
     * @param message   消息
     * @param requestId 请求ID
     */
    public SmsResponse(Boolean success, String code, String message, String requestId) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.requestId = requestId;
    }

    /**
     * 创建成功响应
     *
     * @param message   消息
     * @param requestId 请求ID
     * @param bizId     业务ID
     * @return 响应对象
     */
    public static SmsResponse success(String message, String requestId, String bizId) {
        SmsResponse response = new SmsResponse();
        response.setSuccess(true);
        response.setMessage(message);
        response.setRequestId(requestId);
        response.setBizId(bizId);
        return response;
    }

    /**
     * 创建失败响应
     *
     * @param code    错误码
     * @param message 错误消息
     * @return 响应对象
     */
    public static SmsResponse failure(String code, String message) {
        SmsResponse response = new SmsResponse();
        response.setSuccess(false);
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}