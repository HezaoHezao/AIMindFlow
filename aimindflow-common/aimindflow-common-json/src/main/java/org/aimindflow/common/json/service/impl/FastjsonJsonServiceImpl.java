package org.aimindflow.common.json.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.json.exception.JsonException;
import org.aimindflow.common.json.service.JsonService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Fastjson JSON服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service("fastjsonJsonService")
public class FastjsonJsonServiceImpl implements JsonService {

    @Override
    public String toJson(Object object) {
        try {
            return JSON.toJSONString(object);
        } catch (Exception e) {
            log.error("对象转JSON字符串失败", e);
            throw new JsonException("对象转JSON字符串失败", e);
        }
    }

    @Override
    public String toPrettyJson(Object object) {
        try {
            return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
        } catch (Exception e) {
            log.error("对象转格式化JSON字符串失败", e);
            throw new JsonException("对象转格式化JSON字符串失败", e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JSON字符串转对象失败", e);
            throw new JsonException("JSON字符串转对象失败", e);
        }
    }

    @Override
    public <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("JSON字符串转List失败", e);
            throw new JsonException("JSON字符串转List失败", e);
        }
    }

    @Override
    public Map<String, Object> fromJsonToMap(String json) {
        try {
            return JSON.parseObject(json);
        } catch (Exception e) {
            log.error("JSON字符串转Map失败", e);
            throw new JsonException("JSON字符串转Map失败", e);
        }
    }

    @Override
    public Map<String, Object> toMap(Object object) {
        try {
            String json = toJson(object);
            return fromJsonToMap(json);
        } catch (Exception e) {
            log.error("对象转Map失败", e);
            throw new JsonException("对象转Map失败", e);
        }
    }

    @Override
    public <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        try {
            String json = toJson(map);
            return fromJson(json, clazz);
        } catch (Exception e) {
            log.error("Map转对象失败", e);
            throw new JsonException("Map转对象失败", e);
        }
    }

    @Override
    public <T> T convert(Object source, Class<T> targetClass) {
        try {
            String json = toJson(source);
            return fromJson(json, targetClass);
        } catch (Exception e) {
            log.error("对象转换失败", e);
            throw new JsonException("对象转换失败", e);
        }
    }

    @Override
    public boolean isValidJson(String json) {
        try {
            JSON.parse(json);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }
}