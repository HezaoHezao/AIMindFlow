package org.aimindflow.common.sensitive.handler;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sensitive.enums.SensitiveType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 默认敏感数据处理器实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class DefaultSensitiveHandler implements SensitiveHandler {

    @Override
    public String desensitize(String value, SensitiveType type, String rule) {
        if (!StringUtils.hasText(value)) {
            return value;
        }

        try {
            switch (type) {
                case PHONE:
                    return desensitizePhone(value);
                case EMAIL:
                    return desensitizeEmail(value);
                case ID_CARD:
                    return desensitizeIdCard(value);
                case NAME:
                    return desensitizeName(value);
                case BANK_CARD:
                    return desensitizeBankCard(value);
                case ADDRESS:
                    return desensitizeAddress(value);
                case PASSWORD:
                    return desensitizePassword(value);
                case IP_ADDRESS:
                    return desensitizeIpAddress(value);
                case CAR_LICENSE:
                    return desensitizeCarLicense(value);
                case CUSTOM:
                    return desensitizeCustom(value, rule);
                default:
                    return value;
            }
        } catch (Exception e) {
            log.error("脱敏处理失败，原始值：{}，类型：{}，规则：{}", value, type, rule, e);
            return value;
        }
    }

    @Override
    public boolean supports(SensitiveType type) {
        return type != null;
    }

    /**
     * 手机号脱敏
     * 保留前3位和后4位，中间用****替换
     */
    private String desensitizePhone(String phone) {
        if (phone.length() < 7) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }

    /**
     * 邮箱脱敏
     * 保留@前的前2位和@后的全部，中间用****替换
     */
    private String desensitizeEmail(String email) {
        if (!email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return email;
        }
        
        return username.substring(0, 2) + "****@" + domain;
    }

    /**
     * 身份证号脱敏
     * 保留前4位和后4位，中间用**********替换
     */
    private String desensitizeIdCard(String idCard) {
        if (idCard.length() < 8) {
            return idCard;
        }
        return idCard.substring(0, 4) + "**********" + idCard.substring(idCard.length() - 4);
    }

    /**
     * 姓名脱敏
     * 保留第一个字符，其余用*替换
     */
    private String desensitizeName(String name) {
        if (name.length() <= 1) {
            return name;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            sb.append("*");
        }
        return sb.toString();
    }

    /**
     * 银行卡号脱敏
     * 保留前4位和后4位，中间用****替换
     */
    private String desensitizeBankCard(String bankCard) {
        if (bankCard.length() < 8) {
            return bankCard;
        }
        return bankCard.substring(0, 4) + "****" + bankCard.substring(bankCard.length() - 4);
    }

    /**
     * 地址脱敏
     * 保留前6个字符，其余用****替换
     */
    private String desensitizeAddress(String address) {
        if (address.length() <= 6) {
            return address;
        }
        return address.substring(0, 6) + "****";
    }

    /**
     * 密码脱敏
     * 全部用******替换
     */
    private String desensitizePassword(String password) {
        return "******";
    }

    /**
     * IP地址脱敏
     * 保留前两段，后两段用*替换
     */
    private String desensitizeIpAddress(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return ip;
        }
        return parts[0] + "." + parts[1] + ".*.*";
    }

    /**
     * 车牌号脱敏
     * 保留前2位和后1位，中间用***替换
     */
    private String desensitizeCarLicense(String carLicense) {
        if (carLicense.length() < 3) {
            return carLicense;
        }
        return carLicense.substring(0, 2) + "***" + carLicense.substring(carLicense.length() - 1);
    }

    /**
     * 自定义脱敏
     * 规则格式：startIndex,endIndex,maskChar
     * 例如：3,4,* 表示保留前3位和后4位，中间用*替换
     */
    private String desensitizeCustom(String value, String rule) {
        if (!StringUtils.hasText(rule)) {
            return value;
        }

        try {
            String[] parts = rule.split(",");
            if (parts.length != 3) {
                log.warn("自定义脱敏规则格式错误：{}", rule);
                return value;
            }

            int startIndex = Integer.parseInt(parts[0].trim());
            int endIndex = Integer.parseInt(parts[1].trim());
            String maskChar = parts[2].trim();

            if (startIndex < 0 || endIndex < 0 || startIndex + endIndex >= value.length()) {
                return value;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(value.substring(0, startIndex));
            
            int maskLength = value.length() - startIndex - endIndex;
            for (int i = 0; i < maskLength; i++) {
                sb.append(maskChar);
            }
            
            sb.append(value.substring(value.length() - endIndex));
            return sb.toString();
        } catch (Exception e) {
            log.error("自定义脱敏处理失败，值：{}，规则：{}", value, rule, e);
            return value;
        }
    }
}