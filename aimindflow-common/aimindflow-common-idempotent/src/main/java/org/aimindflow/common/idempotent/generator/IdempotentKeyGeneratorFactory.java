package org.aimindflow.common.idempotent.generator;

import org.aimindflow.common.idempotent.annotation.Idempotent.IdempotentStrategy;
import org.aimindflow.common.idempotent.generator.impl.ParamsKeyGenerator;
import org.aimindflow.common.idempotent.generator.impl.SpelKeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 幂等Key生成器工厂类
 *
 * @author HezaoHezao
 */
@Component
public class IdempotentKeyGeneratorFactory {

    /**
     * 参数Key生成器
     */
    private final ParamsKeyGenerator paramsKeyGenerator;

    /**
     * SpEL表达式Key生成器
     */
    private final SpelKeyGenerator spelKeyGenerator;

    /**
     * 自定义Key生成器Map
     */
    private final Map<String, IdempotentKeyGenerator> customKeyGenerators;

    @Autowired
    public IdempotentKeyGeneratorFactory(ParamsKeyGenerator paramsKeyGenerator,
                                         SpelKeyGenerator spelKeyGenerator,
                                         Map<String, IdempotentKeyGenerator> customKeyGenerators) {
        this.paramsKeyGenerator = paramsKeyGenerator;
        this.spelKeyGenerator = spelKeyGenerator;
        this.customKeyGenerators = customKeyGenerators;
    }

    /**
     * 获取Key生成器
     *
     * @param strategy 策略
     * @return Key生成器
     */
    public IdempotentKeyGenerator getKeyGenerator(IdempotentStrategy strategy) {
        switch (strategy) {
            case PARAMS:
                return paramsKeyGenerator;
            case SPEL:
                return spelKeyGenerator;
            case CUSTOM:
                // 默认使用第一个自定义Key生成器
                if (!customKeyGenerators.isEmpty()) {
                    return customKeyGenerators.values().iterator().next();
                }
                // 如果没有自定义Key生成器，则使用参数Key生成器
                return paramsKeyGenerator;
            default:
                return paramsKeyGenerator;
        }
    }

    /**
     * 获取自定义Key生成器
     *
     * @param name 名称
     * @return Key生成器
     */
    public IdempotentKeyGenerator getCustomKeyGenerator(String name) {
        return customKeyGenerators.get(name);
    }
}