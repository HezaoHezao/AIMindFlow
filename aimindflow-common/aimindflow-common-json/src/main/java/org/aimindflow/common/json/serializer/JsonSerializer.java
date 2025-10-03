package org.aimindflow.common.json.serializer;

/**
 * JSON序列化器接口
 *
 * @author HezaoHezao
 * @param <T> 序列化对象类型
 */
public interface JsonSerializer<T> {

    /**
     * 序列化对象为JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    String serialize(T object);

    /**
     * 反序列化JSON字符串为对象
     *
     * @param json JSON字符串
     * @return 对象
     */
    T deserialize(String json);
}