package org.aimindflow.common.sms.entity;

import lombok.Data;

import java.util.Map;

/**
 * 短信发送请求实体
 *
 * @author HezaoHezao
 */
@Data
public class SmsRequest {

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 模板参数
     */
    private Map<String, Object> templateParams;

    /**
     * 短信签名（可选，如果不传则使用配置中的默认签名）
     */
    private String signName;

    /**
     * 外部流水号（可选，用于幂等性控制）
     */
    private String outId;

    /**
     * 构造函数
     */
    public SmsRequest() {
    }

    /**
     * 构造函数
     *
     * @param phoneNumber  手机号码
     * @param templateCode 模板代码
     */
    public SmsRequest(String phoneNumber, String templateCode) {
        this.phoneNumber = phoneNumber;
        this.templateCode = templateCode;
    }

    /**
     * 构造函数
     *
     * @param phoneNumber    手机号码
     * @param templateCode   模板代码
     * @param templateParams 模板参数
     */
    public SmsRequest(String phoneNumber, String templateCode, Map<String, Object> templateParams) {
        this.phoneNumber = phoneNumber;
        this.templateCode = templateCode;
        this.templateParams = templateParams;
    }
}