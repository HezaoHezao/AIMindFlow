package org.aimindflow.common.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.regex.Pattern;

/**
 * 密码工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class PasswordUtils {

    @Autowired
    private SecurityProperties securityProperties;

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtils() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 加密密码
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encodePassword(String rawPassword) {
        try {
            return passwordEncoder.encode(rawPassword);
        } catch (Exception e) {
            log.error("密码加密失败", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 验证密码
     *
     * @param rawPassword     原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            return passwordEncoder.matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.error("密码验证失败", e);
            return false;
        }
    }

    /**
     * 验证密码强度
     *
     * @param password 密码
     * @return 验证结果
     */
    public PasswordValidationResult validatePassword(String password) {
        PasswordValidationResult result = new PasswordValidationResult();
        
        if (password == null || password.isEmpty()) {
            result.setValid(false);
            result.setMessage("密码不能为空");
            return result;
        }

        SecurityProperties.Password passwordConfig = securityProperties.getPassword();

        // 检查长度
        if (password.length() < passwordConfig.getMinLength()) {
            result.setValid(false);
            result.setMessage("密码长度不能少于" + passwordConfig.getMinLength() + "位");
            return result;
        }

        if (password.length() > passwordConfig.getMaxLength()) {
            result.setValid(false);
            result.setMessage("密码长度不能超过" + passwordConfig.getMaxLength() + "位");
            return result;
        }

        // 检查是否包含数字
        if (passwordConfig.getRequireDigit() && !Pattern.compile(".*\\d.*").matcher(password).matches()) {
            result.setValid(false);
            result.setMessage("密码必须包含至少一个数字");
            return result;
        }

        // 检查是否包含字母
        if (passwordConfig.getRequireLetter() && !Pattern.compile(".*[a-zA-Z].*").matcher(password).matches()) {
            result.setValid(false);
            result.setMessage("密码必须包含至少一个字母");
            return result;
        }

        // 检查是否包含特殊字符
        if (passwordConfig.getRequireSpecialChar() && 
            !Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*").matcher(password).matches()) {
            result.setValid(false);
            result.setMessage("密码必须包含至少一个特殊字符");
            return result;
        }

        result.setValid(true);
        result.setMessage("密码强度验证通过");
        return result;
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    /**
     * 生成符合策略的随机密码
     *
     * @return 随机密码
     */
    public String generateRandomPassword() {
        SecurityProperties.Password passwordConfig = securityProperties.getPassword();
        int length = Math.max(passwordConfig.getMinLength(), 8);
        
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialChars = "!@#$%^&*";
        
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // 确保包含必需的字符类型
        if (passwordConfig.getRequireLetter()) {
            password.append(letters.charAt(random.nextInt(letters.length())));
        }
        if (passwordConfig.getRequireDigit()) {
            password.append(digits.charAt(random.nextInt(digits.length())));
        }
        if (passwordConfig.getRequireSpecialChar()) {
            password.append(specialChars.charAt(random.nextInt(specialChars.length())));
        }

        // 填充剩余长度
        String allChars = letters + digits + (passwordConfig.getRequireSpecialChar() ? specialChars : "");
        while (password.length() < length) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // 打乱字符顺序
        return shuffleString(password.toString());
    }

    /**
     * 打乱字符串
     *
     * @param input 输入字符串
     * @return 打乱后的字符串
     */
    private String shuffleString(String input) {
        char[] chars = input.toCharArray();
        SecureRandom random = new SecureRandom();
        
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        
        return new String(chars);
    }

    /**
     * 密码验证结果类
     */
    public static class PasswordValidationResult {
        private boolean valid;
        private String message;

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}