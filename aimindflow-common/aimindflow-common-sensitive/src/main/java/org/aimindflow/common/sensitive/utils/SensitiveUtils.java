package org.aimindflow.common.sensitive.utils;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.sensitive.annotation.Sensitive;
import org.aimindflow.common.sensitive.handler.SensitiveHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

/**
 * 敏感数据工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class SensitiveUtils {

    @Autowired
    private SensitiveHandler sensitiveHandler;

    /**
     * 对象脱敏处理
     *
     * @param obj 待处理对象
     * @return 处理后的对象
     */
    public Object desensitize(Object obj) {
        if (obj == null) {
            return null;
        }

        try {
            // 处理集合类型
            if (obj instanceof Collection) {
                Collection<?> collection = (Collection<?>) obj;
                for (Object item : collection) {
                    desensitize(item);
                }
                return obj;
            }

            // 处理Map类型
            if (obj instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) obj;
                for (Object value : map.values()) {
                    desensitize(value);
                }
                return obj;
            }

            // 处理普通对象
            Class<?> clazz = obj.getClass();
            
            // 跳过基本类型和包装类型
            if (isBasicType(clazz)) {
                return obj;
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                
                Sensitive sensitive = field.getAnnotation(Sensitive.class);
                if (sensitive != null && sensitive.enabled()) {
                    Object fieldValue = field.get(obj);
                    if (fieldValue instanceof String) {
                        String desensitizedValue = sensitiveHandler.desensitize(
                            (String) fieldValue, 
                            sensitive.type(), 
                            sensitive.rule()
                        );
                        field.set(obj, desensitizedValue);
                    }
                } else {
                    // 递归处理嵌套对象
                    Object fieldValue = field.get(obj);
                    if (fieldValue != null && !isBasicType(fieldValue.getClass())) {
                        desensitize(fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            log.error("对象脱敏处理失败", e);
        }

        return obj;
    }

    /**
     * 字符串脱敏处理
     *
     * @param value 原始值
     * @param type  敏感数据类型
     * @return 脱敏后的值
     */
    public String desensitize(String value, org.aimindflow.common.sensitive.enums.SensitiveType type) {
        return sensitiveHandler.desensitize(value, type, null);
    }

    /**
     * 字符串脱敏处理（自定义规则）
     *
     * @param value 原始值
     * @param type  敏感数据类型
     * @param rule  自定义规则
     * @return 脱敏后的值
     */
    public String desensitize(String value, org.aimindflow.common.sensitive.enums.SensitiveType type, String rule) {
        return sensitiveHandler.desensitize(value, type, rule);
    }

    /**
     * 判断是否为基本类型
     *
     * @param clazz 类型
     * @return 是否为基本类型
     */
    private boolean isBasicType(Class<?> clazz) {
        return clazz.isPrimitive() ||
               clazz == String.class ||
               clazz == Boolean.class ||
               clazz == Character.class ||
               clazz == Byte.class ||
               clazz == Short.class ||
               clazz == Integer.class ||
               clazz == Long.class ||
               clazz == Float.class ||
               clazz == Double.class ||
               clazz.isEnum() ||
               clazz.getPackage() != null && clazz.getPackage().getName().startsWith("java.");
    }
}