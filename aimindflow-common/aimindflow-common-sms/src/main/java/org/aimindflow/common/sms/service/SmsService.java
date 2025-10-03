package org.aimindflow.common.sms.service;

import org.aimindflow.common.sms.entity.SmsRequest;
import org.aimindflow.common.sms.entity.SmsResponse;

import java.util.Map;

/**
 * 短信服务接口
 *
 * @author HezaoHezao
 */
public interface SmsService {

    /**
     * 发送短信
     *
     * @param request 短信发送请求
     * @return 发送结果
     */
    SmsResponse sendSms(SmsRequest request);

    /**
     * 发送短信（简化方法）
     *
     * @param phoneNumber  手机号码
     * @param templateCode 模板代码
     * @return 发送结果
     */
    SmsResponse sendSms(String phoneNumber, String templateCode);

    /**
     * 发送短信（简化方法）
     *
     * @param phoneNumber    手机号码
     * @param templateCode   模板代码
     * @param templateParams 模板参数
     * @return 发送结果
     */
    SmsResponse sendSms(String phoneNumber, String templateCode, Map<String, Object> templateParams);

    /**
     * 发送验证码短信
     *
     * @param phoneNumber 手机号码
     * @param code        验证码
     * @return 发送结果
     */
    SmsResponse sendVerificationCode(String phoneNumber, String code);

    /**
     * 批量发送短信
     *
     * @param phoneNumbers 手机号码列表
     * @param templateCode 模板代码
     * @return 发送结果
     */
    SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode);

    /**
     * 批量发送短信
     *
     * @param phoneNumbers   手机号码列表
     * @param templateCode   模板代码
     * @param templateParams 模板参数
     * @return 发送结果
     */
    SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode, Map<String, Object> templateParams);
}