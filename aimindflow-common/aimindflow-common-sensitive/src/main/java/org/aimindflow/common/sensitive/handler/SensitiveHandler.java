package org.aimindflow.common.sensitive.handler;

import org.aimindflow.common.sensitive.enums.SensitiveType;

/**
 * 敏感数据处理器接口
 *
 * @author HezaoHezao
 */
public interface SensitiveHandler {

    /**
     * 脱敏处理
     *
     * @param value 原始值
     * @param type  敏感数据类型
     * @param rule  自定义规则（当type为CUSTOM时使用）
     * @return 脱敏后的值
     */
    String desensitize(String value, SensitiveType type, String rule);

    /**
     * 判断是否支持该类型的脱敏
     *
     * @param type 敏感数据类型
     * @return 是否支持
     */
    boolean supports(SensitiveType type);
}