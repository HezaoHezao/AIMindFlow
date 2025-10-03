package org.aimindflow.common.json.service;

import java.util.List;
import java.util.Map;

/**
 * JSON服务接口
 *
 * @author HezaoHezao
 */
public interface JsonService {

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    String toJson(Object object);

    /**
     * 对象转JSON字符串（格式化）
     *
     * @param object 对象
     * @return 格式化的JSON字符串
     */
    String toPrettyJson(Object object);

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象
     */
    <T> T fromJson(String json, Class<T> clazz);

    /**
     * JSON字符串转List
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象列表
     */
    <T> List<T> fromJsonToList(String json, Class<T> clazz);

    /**
     * JSON字符串转Map
     *
     * @param json JSON字符串
     * @return Map对象
     */
    Map<String, Object> fromJsonToMap(String json);

    /**
     * 对象转Map
     *
     * @param object 对象
     * @return Map对象
     */
    Map<String, Object> toMap(Object object);

    /**
     * Map转对象
     *
     * @param map   Map对象
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象
     */
    <T> T fromMap(Map<String, Object> map, Class<T> clazz);

    /**
     * 对象转换
     *
     * @param source    源对象
     * @param targetClass 目标类型
     * @param <T>       目标泛型
     * @return 目标对象
     */
    <T> T convert(Object source, Class<T> targetClass);

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param json JSON字符串
     * @return 是否有效
     */
    boolean isValidJson(String json);
}