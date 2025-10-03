package org.aimindflow.common.translation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.translation.service.TranslationService;

/**
 * 默认翻译服务实现
 *
 * 该实现返回原始值，作为占位实现。实际项目中可通过数据库、缓存、远程服务等进行真实翻译。
 *
 * @author HezaoHezao
 */
@Slf4j
public class DefaultTranslationService implements TranslationService {

    @Override
    public String translate(String type, String key, String code) {
        // 默认实现：直接返回原始值，便于后续扩展替换
        return code;
    }
}