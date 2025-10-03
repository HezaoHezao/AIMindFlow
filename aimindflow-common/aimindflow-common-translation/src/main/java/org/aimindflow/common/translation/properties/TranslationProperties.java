package org.aimindflow.common.translation.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 翻译模块配置
 *
 * 可通过配置开启或关闭统一翻译拦截。
 *
 * @author HezaoHezao
 */
@ConfigurationProperties(prefix = "translation")
public class TranslationProperties {

    /**
     * 是否启用响应拦截进行统一翻译
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}