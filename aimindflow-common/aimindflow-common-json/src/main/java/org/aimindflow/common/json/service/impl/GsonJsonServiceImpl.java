package org.aimindflow.common.json.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.json.exception.JsonException;
import org.aimindflow.common.json.service.JsonService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gson JSON服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service("gsonJsonService")
public class GsonJsonServiceImpl implements JsonService {

    private final Gson gson;
    private final Gson prettyGson;

    public GsonJsonServiceImpl() {
        this.gson = new Gson();
        this.prettyGson = new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            log.error("对象转JSON字符串失败", e);
            throw new JsonException("对象转JSON字符串失败", e);
        }
    }

    @Override
    public String toPrettyJson(Object object) {
        try {
            return prettyGson.toJson(object);
        } catch (Exception e) {
            log.error("对象转格式化JSON字符串失败", e);
            throw new JsonException("对象转格式化JSON字符串失败", e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            log.error("JSON字符串转对象失败", e);
            throw new JsonException("JSON字符串转对象失败", e);
        }
    }

    @Override
    public <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            log.error("JSON字符串转List失败", e);
            throw new JsonException("JSON字符串转List失败", e);
        }
    }

    @Override
    public Map<String, Object> fromJsonToMap(String json) {
        try {
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            return gson.fromJson(json, mapType);
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
            JsonElement jsonElement = JsonParser.parseString(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}