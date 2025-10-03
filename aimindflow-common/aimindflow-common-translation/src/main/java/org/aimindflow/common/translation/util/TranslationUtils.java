package org.aimindflow.common.translation.util;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.translation.annotation.Translation;
import org.aimindflow.common.translation.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * 翻译工具类
 *
 * 提供对对象的递归扫描与字段翻译能力，支持集合与嵌套对象。
 *
 * @author HezaoHezao
 */
@Slf4j
public class TranslationUtils {

    @Autowired
    private TranslationService translationService;

    /**
     * 对对象进行递归翻译处理
     *
     * @param obj 待处理对象
     */
    public void translateObject(Object obj) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Collection) {
            for (Object item : (Collection<?>) obj) {
                translateObject(item);
            }
            return;
        }
        if (obj instanceof Map) {
            for (Object value : ((Map<?, ?>) obj).values()) {
                translateObject(value);
            }
            return;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                // 递归处理嵌套对象
                if (value != null && !isPrimitiveOrString(value.getClass())) {
                    translateObject(value);
                }

                // 处理带有 @Translation 注解的字段
                Translation translation = field.getAnnotation(Translation.class);
                if (translation != null && value != null) {
                    String type = translation.type();
                    String key = translation.key();
                    String code = String.valueOf(value);
                    String display = translationService.translate(type, key, code);
                    field.set(obj, display);
                }
            } catch (IllegalAccessException e) {
                log.warn("翻译处理字段异常: {}.{}", clazz.getSimpleName(), field.getName(), e);
            }
        }
    }

    private boolean isPrimitiveOrString(Class<?> type) {
        return type.isPrimitive() || String.class.equals(type)
            || Number.class.isAssignableFrom(type)
            || Boolean.class.equals(type) || Character.class.equals(type);
    }
}