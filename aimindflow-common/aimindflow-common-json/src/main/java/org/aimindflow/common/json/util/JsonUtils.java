package org.aimindflow.common.json.util;

import org.aimindflow.common.json.factory.JsonServiceFactory;
import org.aimindflow.common.json.service.JsonService;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author HezaoHezao
 */
public class JsonUtils {

    /**
     * 对象转JSON字符串
     *
     * @param object 对象
     * @return JSON字符串
     */
    public static String toJson(Object object) {
        return JsonServiceFactory.getJsonService().toJson(object);
    }

    /**
     * 对象转JSON字符串（格式化）
     *
     * @param object 对象
     * @return 格式化的JSON字符串
     */
    public static String toPrettyJson(Object object) {
        return JsonServiceFactory.getJsonService().toPrettyJson(object);
    }

    /**
     * JSON字符串转对象
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return JsonServiceFactory.getJsonService().fromJson(json, clazz);
    }

    /**
     * JSON字符串转List
     *
     * @param json  JSON字符串
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象列表
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        return JsonServiceFactory.getJsonService().fromJsonToList(json, clazz);
    }

    /**
     * JSON字符串转Map
     *
     * @param json JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        return JsonServiceFactory.getJsonService().fromJsonToMap(json);
    }

    /**
     * 对象转Map
     *
     * @param object 对象
     * @return Map对象
     */
    public static Map<String, Object> toMap(Object object) {
        return JsonServiceFactory.getJsonService().toMap(object);
    }

    /**
     * Map转对象
     *
     * @param map   Map对象
     * @param clazz 对象类型
     * @param <T>   对象泛型
     * @return 对象
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        return JsonServiceFactory.getJsonService().fromMap(map, clazz);
    }

    /**
     * 对象转换
     *
     * @param source      源对象
     * @param targetClass 目标类型
     * @param <T>         目标泛型
     * @return 目标对象
     */
    public static <T> T convert(Object source, Class<T> targetClass) {
        return JsonServiceFactory.getJsonService().convert(source, targetClass);
    }

    /**
     * 判断字符串是否为有效的JSON
     *
     * @param json JSON字符串
     * @return 是否有效
     */
    public static boolean isValidJson(String json) {
        return JsonServiceFactory.getJsonService().isValidJson(json);
    }

    /**
     * 使用Jackson处理JSON
     *
     * @return Jackson JSON服务
     */
    public static JsonService jackson() {
        return JsonServiceFactory.getJacksonJsonService();
    }

    /**
     * 使用Gson处理JSON
     *
     * @return Gson JSON服务
     */
    public static JsonService gson() {
        return JsonServiceFactory.getGsonJsonService();
    }

    /**
     * 使用Fastjson处理JSON
     *
     * @return Fastjson JSON服务
     */
    public static JsonService fastjson() {
        return JsonServiceFactory.getFastjsonJsonService();
    }
}