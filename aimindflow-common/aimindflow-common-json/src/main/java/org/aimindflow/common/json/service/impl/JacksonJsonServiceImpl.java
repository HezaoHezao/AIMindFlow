package org.aimindflow.common.json.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.json.exception.JsonException;
import org.aimindflow.common.json.service.JsonService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jackson JSON服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service("jacksonJsonService")
public class JacksonJsonServiceImpl implements JsonService {

    private final ObjectMapper objectMapper;

    public JacksonJsonServiceImpl() {
        this.objectMapper = new ObjectMapper();
        // 注册Java8日期时间模块
        objectMapper.findAndRegisterModules();
    }

    @Override
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转JSON字符串失败", e);
            throw new JsonException("对象转JSON字符串失败", e);
        }
    }

    @Override
    public String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("对象转格式化JSON字符串失败", e);
            throw new JsonException("对象转格式化JSON字符串失败", e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转对象失败", e);
            throw new JsonException("JSON字符串转对象失败", e);
        }
    }

    @Override
    public <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (JsonProcessingException e) {
            log.error("JSON字符串转List失败", e);
            throw new JsonException("JSON字符串转List失败", e);
        }
    }

    @Override
    public Map<String, Object> fromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
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
            return objectMapper.convertValue(map, clazz);
        } catch (Exception e) {
            log.error("Map转对象失败", e);
            throw new JsonException("Map转对象失败", e);
        }
    }

    @Override
    public <T> T convert(Object source, Class<T> targetClass) {
        try {
            return objectMapper.convertValue(source, targetClass);
        } catch (Exception e) {
            log.error("对象转换失败", e);
            throw new JsonException("对象转换失败", e);
        }
    }

    @Override
    public boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}