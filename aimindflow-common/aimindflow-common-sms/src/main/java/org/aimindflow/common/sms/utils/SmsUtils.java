package org.aimindflow.common.sms.utils;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sms.entity.SmsRequest;
import org.aimindflow.common.sms.entity.SmsResponse;
import org.aimindflow.common.sms.limiter.SmsLimiter;
import org.aimindflow.common.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 短信工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class SmsUtils {

    @Autowired
    private SmsService smsService;

    @Autowired
    private SmsLimiter smsLimiter;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");
    private static final Random RANDOM = new Random();

    /**
     * 发送短信（带限流检查）
     *
     * @param request 短信发送请求
     * @return 发送结果
     */
    public SmsResponse sendSmsWithLimit(SmsRequest request) {
        // 验证手机号格式
        if (!isValidPhoneNumber(request.getPhoneNumber())) {
            return SmsResponse.failure("INVALID_PHONE", "手机号格式不正确");
        }

        // 获取客户端IP
        String clientIp = getClientIp();

        // 检查手机号限制
        if (!smsLimiter.checkPhoneLimit(request.getPhoneNumber())) {
            return SmsResponse.failure("PHONE_LIMIT_EXCEEDED", "手机号发送频率超限，请稍后再试");
        }

        // 检查IP限制
        if (!smsLimiter.checkIpLimit(clientIp)) {
            return SmsResponse.failure("IP_LIMIT_EXCEEDED", "IP发送频率超限，请稍后再试");
        }

        // 发送短信
        SmsResponse response = smsService.sendSms(request);

        // 如果发送成功，记录发送次数
        if (response.isSuccess()) {
            smsLimiter.recordSend(request.getPhoneNumber(), clientIp);
        }

        return response;
    }

    /**
     * 发送验证码短信（带限流检查）
     *
     * @param phoneNumber 手机号码
     * @param code        验证码
     * @return 发送结果
     */
    public SmsResponse sendVerificationCodeWithLimit(String phoneNumber, String code) {
        SmsRequest request = new SmsRequest();
        request.setPhoneNumber(phoneNumber);
        request.setTemplateCode("SMS_VERIFICATION_CODE"); // 需要根据实际模板调整
        request.setTemplateParams(Map.of("code", code));
        
        return sendSmsWithLimit(request);
    }

    /**
     * 生成数字验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public String generateNumericCode(int length) {
        if (length <= 0 || length > 10) {
            throw new IllegalArgumentException("验证码长度必须在1-10之间");
        }

        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }

    /**
     * 生成字母数字混合验证码
     *
     * @param length 验证码长度
     * @return 验证码
     */
    public String generateAlphanumericCode(int length) {
        if (length <= 0 || length > 20) {
            throw new IllegalArgumentException("验证码长度必须在1-20之间");
        }

        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return code.toString();
    }

    /**
     * 验证手机号格式
     *
     * @param phoneNumber 手机号码
     * @return 是否有效
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        return StringUtils.hasText(phoneNumber) && PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * 手机号脱敏
     *
     * @param phoneNumber 手机号码
     * @return 脱敏后的手机号
     */
    public String maskPhoneNumber(String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber) || phoneNumber.length() != 11) {
            return phoneNumber;
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7);
    }

    /**
     * 获取客户端IP地址
     *
     * @return IP地址
     */
    private String getClientIp() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                return "unknown";
            }

            HttpServletRequest request = attributes.getRequest();
            String ip = request.getHeader("X-Forwarded-For");
            
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }

            // 处理多个IP的情况，取第一个
            if (ip != null && ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }

            return ip;
        } catch (Exception e) {
            log.warn("获取客户端IP异常", e);
            return "unknown";
        }
    }

    /**
     * 清除手机号发送限制
     *
     * @param phoneNumber 手机号码
     */
    public void clearPhoneLimit(String phoneNumber) {
        smsLimiter.clearPhoneLimit(phoneNumber);
    }

    /**
     * 清除IP发送限制
     *
     * @param ipAddress IP地址
     */
    public void clearIpLimit(String ipAddress) {
        smsLimiter.clearIpLimit(ipAddress);
    }

    /**
     * 批量发送短信（带限流检查）
     *
     * @param phoneNumbers   手机号码列表
     * @param templateCode   模板代码
     * @param templateParams 模板参数
     * @return 发送结果
     */
    public SmsResponse sendBatchSmsWithLimit(String[] phoneNumbers, String templateCode, Map<String, Object> templateParams) {
        // 验证手机号格式
        for (String phoneNumber : phoneNumbers) {
            if (!isValidPhoneNumber(phoneNumber)) {
                return SmsResponse.failure("INVALID_PHONE", "手机号格式不正确: " + phoneNumber);
            }
        }

        // 获取客户端IP
        String clientIp = getClientIp();

        // 检查每个手机号的限制
        for (String phoneNumber : phoneNumbers) {
            if (!smsLimiter.checkPhoneLimit(phoneNumber)) {
                return SmsResponse.failure("PHONE_LIMIT_EXCEEDED", 
                    "手机号 " + maskPhoneNumber(phoneNumber) + " 发送频率超限");
            }
        }

        // 检查IP限制
        if (!smsLimiter.checkIpLimit(clientIp)) {
            return SmsResponse.failure("IP_LIMIT_EXCEEDED", "IP发送频率超限，请稍后再试");
        }

        // 发送短信
        SmsResponse response = smsService.sendBatchSms(phoneNumbers, templateCode, templateParams);

        // 如果发送成功，记录每个手机号的发送次数
        if (response.isSuccess()) {
            for (String phoneNumber : phoneNumbers) {
                smsLimiter.recordSend(phoneNumber, clientIp);
            }
        }

        return response;
    }
}