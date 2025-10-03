package org.aimindflow.common.translation.service;

/**
 * 翻译服务接口
 *
 * 提供统一的字段值翻译能力，不同类型的翻译器由实现类提供。
 *
 * @author HezaoHezao
 */
public interface TranslationService {

    /**
     * 根据类型与原始值进行翻译
     *
     * @param type 翻译类型（如：dict、user、dept）
     * @param key  备用键或字典类型
     * @param code 原始值（如：字典编码、用户ID）
     * @return 翻译后的展示值，如果无法翻译则返回原始值
     */
    String translate(String type, String key, String code);
}