package org.aimindflow.common.sms.service.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

/**
 * 阿里云短信服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "aimindflow.sms", name = "provider", havingValue = "ALIYUN", matchIfMissing = true)
public class AliyunSmsServiceImpl implements SmsService {

    @Autowired
    private SmsProperties smsProperties;

    private Client client;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 初始化阿里云短信客户端
     */
    @PostConstruct
    public void init() {
        try {
            SmsProperties.Aliyun aliyun = smsProperties.getAliyun();
            Config config = new Config()
                    .setAccessKeyId(aliyun.getAccessKeyId())
                    .setAccessKeySecret(aliyun.getAccessKeySecret())
                    .setEndpoint(aliyun.getEndpoint());
            
            this.client = new Client(config);
            log.info("阿里云短信服务初始化成功");
        } catch (Exception e) {
            log.error("阿里云短信服务初始化失败", e);
            throw new RuntimeException("阿里云短信服务初始化失败", e);
        }
    }

    @Override
    public SmsResponse sendSms(SmsRequest request) {
        try {
            // 构建发送请求
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setPhoneNumbers(request.getPhoneNumber());
            sendSmsRequest.setTemplateCode(request.getTemplateCode());
            
            // 设置签名
            String signName = StringUtils.hasText(request.getSignName()) ? 
                request.getSignName() : smsProperties.getAliyun().getSignName();
            sendSmsRequest.setSignName(signName);
            
            // 设置模板参数
            if (request.getTemplateParams() != null && !request.getTemplateParams().isEmpty()) {
                String templateParam = objectMapper.writeValueAsString(request.getTemplateParams());
                sendSmsRequest.setTemplateParam(templateParam);
            }
            
            // 设置外部流水号
            if (StringUtils.hasText(request.getOutId())) {
                sendSmsRequest.setOutId(request.getOutId());
            }

            // 发送短信
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            
            // 处理响应
            if ("OK".equals(response.getBody().getCode())) {
                log.info("阿里云短信发送成功，手机号：{}，模板：{}，请求ID：{}", 
                    request.getPhoneNumber(), request.getTemplateCode(), response.getBody().getRequestId());
                return SmsResponse.success("短信发送成功", 
                    response.getBody().getRequestId(), response.getBody().getBizId());
            } else {
                log.warn("阿里云短信发送失败，手机号：{}，错误码：{}，错误信息：{}", 
                    request.getPhoneNumber(), response.getBody().getCode(), response.getBody().getMessage());
                return SmsResponse.failure(response.getBody().getCode(), response.getBody().getMessage());
            }
        } catch (Exception e) {
            log.error("阿里云短信发送异常，手机号：{}", request.getPhoneNumber(), e);
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
        return sendSms(phoneNumber, "SMS_VERIFICATION_CODE", params);
    }

    @Override
    public SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode) {
        return sendBatchSms(phoneNumbers, templateCode, null);
    }

    @Override
    public SmsResponse sendBatchSms(String[] phoneNumbers, String templateCode, Map<String, Object> templateParams) {
        try {
            // 构建批量发送请求
            SendSmsRequest sendSmsRequest = new SendSmsRequest();
            sendSmsRequest.setPhoneNumbers(String.join(",", phoneNumbers));
            sendSmsRequest.setTemplateCode(templateCode);
            sendSmsRequest.setSignName(smsProperties.getAliyun().getSignName());
            
            // 设置模板参数
            if (templateParams != null && !templateParams.isEmpty()) {
                String templateParam = objectMapper.writeValueAsString(templateParams);
                sendSmsRequest.setTemplateParam(templateParam);
            }

            // 发送短信
            SendSmsResponse response = client.sendSms(sendSmsRequest);
            
            // 处理响应
            if ("OK".equals(response.getBody().getCode())) {
                log.info("阿里云批量短信发送成功，手机号数量：{}，模板：{}，请求ID：{}", 
                    phoneNumbers.length, templateCode, response.getBody().getRequestId());
                return SmsResponse.success("批量短信发送成功", 
                    response.getBody().getRequestId(), response.getBody().getBizId());
            } else {
                log.warn("阿里云批量短信发送失败，错误码：{}，错误信息：{}", 
                    response.getBody().getCode(), response.getBody().getMessage());
                return SmsResponse.failure(response.getBody().getCode(), response.getBody().getMessage());
            }
        } catch (Exception e) {
            log.error("阿里云批量短信发送异常", e);
            return SmsResponse.failure("SYSTEM_ERROR", "批量短信发送异常：" + e.getMessage());
        }
    }
}