package org.aimindflow.common.sms.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20210111.models.SendStatus;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sms.entity.SmsRequest;
import org.aimindflow.common.sms.entity.SmsResponse;
import org.aimindflow.common.sms.properties.SmsProperties;
import org.aimindflow.common.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 腾讯云短信服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "aimindflow.sms", name = "provider", havingValue = "TENCENT")
public class TencentSmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    private SmsClient client;

    /**
     * 初始化腾讯云短信客户端
     */
    @PostConstruct
    public void init() {
        try {
            SmsProperties.Tencent tencent = smsProperties.getTencent();
            
            // 实例化一个认证对象
            Credential cred = new Credential(tencent.getSecretId(), tencent.getSecretKey());
            
            // 实例化一个http选项
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(tencent.getEndpoint());
            
            // 实例化一个client选项
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            
            // 实例化要请求产品的client对象
            this.client = new SmsClient(cred, tencent.getRegion(), clientProfile);
            
            log.info("腾讯云短信服务初始化成功");
        } catch (Exception e) {
            log.error("腾讯云短信服务初始化失败", e);
            throw new RuntimeException("腾讯云短信服务初始化失败", e);
        }
    }

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        try {
            SmsProperties.Tencent tencent = smsProperties.getTencent();
            
            // 实例化一个请求对象
            SendSmsRequest req = new SendSmsRequest();
            
            // 设置短信应用ID
            req.setSmsSdkAppId(tencent.getSdkAppId());
            
            // 设置短信签名内容
            String signName = StringUtils.hasText(request.getSignName()) ? 
                request.getSignName() : tencent.getSignName();
            req.setSignName(signName);
            
            // 设置模板ID
            req.setTemplateId(request.getTemplateCode());
            
            // 设置下发手机号码
            String[] phoneNumberSet = {"+86" + request.getPhoneNumber()};
            req.setPhoneNumberSet(phoneNumberSet);
            
            // 设置模板参数
            if (request.getTemplateParams() != null && !request.getTemplateParams().isEmpty()) {
                List<String> templateParamSet = new ArrayList<>();
                for (Object value : request.getTemplateParams().values()) {
                    templateParamSet.add(String.valueOf(value));
                }
                req.setTemplateParamSet(templateParamSet.toArray(new String[0]));
            }
            
            // 设置扩展码
            if (StringUtils.hasText(request.getOutId())) {
                req.setExtendCode(request.getOutId());
            }

            // 发送短信
            SendSmsResponse resp = client.SendSms(req);
            
            // 处理响应
            SendStatus[] sendStatusSet = resp.getSendStatusSet();
            if (sendStatusSet != null && sendStatusSet.length > 0) {
                SendStatus sendStatus = sendStatusSet[0];
                if ("Ok".equals(sendStatus.getCode())) {
                    log.info("腾讯云短信发送成功，手机号：{}，模板：{}，请求ID：{}", 
                        request.getPhoneNumber(), request.getTemplateCode(), resp.getRequestId());
                    return SmsResponse.success("短信发送成功", 
                        resp.getRequestId(), sendStatus.getSerialNo());
                } else {
                    log.warn("腾讯云短信发送失败，手机号：{}，错误码：{}，错误信息：{}", 
                        request.getPhoneNumber(), sendStatus.getCode(), sendStatus.getMessage());
                    return SmsResponse.failure(sendStatus.getCode(), sendStatus.getMessage());
                }
            } else {
                log.warn("腾讯云短信发送失败，手机号：{}，响应为空", request.getPhoneNumber());
                return SmsResponse.failure("EMPTY_RESPONSE", "短信发送响应为空");
            }
        } catch (Exception e) {
            log.error("腾讯云短信发送异常，手机号：{}", request.getPhoneNumber(), e);
            return SmsResponse.failure("SYSTEM_ERROR", "短信发送异常：" + e.getMessage());
        }
    }

    @Override
    public SmsResponse sendSms(String phoneNumber, String templateCode) {
        SmsRequest request = new SmsRequest(phoneNumber, templateCode);
        return sendSms(request);
    }

    @Override
    public SmsResponse sendSms(String phoneNumber, String templateCode, Map<String, Object> templateParams) {
        SmsRequest request = new SmsRequest(phoneNumber, templateCode, templateParams);
        return sendSms(request);
    }

    @Override
    public SmsResponse sendVerificationCode(String phoneNumber, String code) {
        // 这里需要根据实际的验证码模板进行调整
        Map<String, Object> params = Map.of("code", code);
        return sendSms(phoneNumber, "VERIFICATION_CODE_TEMPLATE", params);
    }

    @Override
    public SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode) {
        return sendBatchSms(phoneNumbers, templateCode, null);
    }

    @Override
    public SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode, Map<String, Object> templateParams) {
        try {
            SmsProperties.Tencent tencent = smsProperties.getTencent();
            
            // 实例化一个请求对象
            SendSmsRequest req = new SendSmsRequest();
            
            // 设置短信应用ID
            req.setSmsSdkAppId(tencent.getSdkAppId());
            
            // 设置短信签名内容
            req.setSignName(tencent.getSignName());
            
            // 设置模板ID
            req.setTemplateId(templateCode);
            
            // 设置下发手机号码（添加+86前缀）
            String[] phoneNumberSet = new String[phoneNumbers.length];
            for (int i = 0; i < phoneNumbers.length; i++) {
                phoneNumberSet[i] = "+86" + phoneNumbers[i];
            }
            req.setPhoneNumberSet(phoneNumberSet);
            
            // 设置模板参数
            if (templateParams != null && !templateParams.isEmpty()) {
                List<String> templateParamSet = new ArrayList<>();
                for (Object value : templateParams.values()) {
                    templateParamSet.add(String.valueOf(value));
                }
                req.setTemplateParamSet(templateParamSet.toArray(new String[0]));
            }

            // 发送短信
            SendSmsResponse resp = client.SendSms(req);
            
            // 处理响应
            SendStatus[] sendStatusSet = resp.getSendStatusSet();
            if (sendStatusSet != null && sendStatusSet.length > 0) {
                // 检查是否所有短信都发送成功
                boolean allSuccess = true;
                StringBuilder errorMessages = new StringBuilder();
                
                for (SendStatus sendStatus : sendStatusSet) {
                    if (!"Ok".equals(sendStatus.getCode())) {
                        allSuccess = false;
                        errorMessages.append(sendStatus.getCode()).append(":").append(sendStatus.getMessage()).append(";");
                    }
                }
                
                if (allSuccess) {
                    log.info("腾讯云批量短信发送成功，手机号数量：{}，模板：{}，请求ID：{}", 
                        phoneNumbers.length, templateCode, resp.getRequestId());
                    return SmsResponse.success("批量短信发送成功", resp.getRequestId(), null);
                } else {
                    log.warn("腾讯云批量短信部分发送失败，错误信息：{}", errorMessages.toString());
                    return SmsResponse.failure("PARTIAL_FAILURE", "部分短信发送失败：" + errorMessages.toString());
                }
            } else {
                log.warn("腾讯云批量短信发送失败，响应为空");
                return SmsResponse.failure("EMPTY_RESPONSE", "批量短信发送响应为空");
            }
        } catch (Exception e) {
            log.error("腾讯云批量短信发送异常", e);
            return SmsResponse.failure("SYSTEM_ERROR", "批量短信发送异常：" + e.getMessage());
        }
    }
}