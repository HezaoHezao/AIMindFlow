package org.aimindflow.common.json.controller;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.json.service.JsonService;
import org.aimindflow.common.json.util.JsonUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * JSON控制器
 *
 * @author HezaoHezao
 */
@Slf4j
@RestController
@RequestMapping("/json")
public class JsonController {

    /**
     * 格式化JSON
     *
     * @param json JSON字符串
     * @return 格式化后的JSON字符串
     */
    @PostMapping("/format")
    public R<String> formatJson(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        Object jsonObject = JsonUtils.fromJson(json, Object.class);
        String prettyJson = JsonUtils.toPrettyJson(jsonObject);
        return R.ok(prettyJson);
    }

    /**
     * 压缩JSON
     *
     * @param json JSON字符串
     * @return 压缩后的JSON字符串
     */
    @PostMapping("/compress")
    public R<String> compressJson(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        Object jsonObject = JsonUtils.fromJson(json, Object.class);
        String compressedJson = JsonUtils.toJson(jsonObject);
        return R.ok(compressedJson);
    }

    /**
     * 验证JSON
     *
     * @param json JSON字符串
     * @return 是否有效
     */
    @PostMapping("/validate")
    public R<Boolean> validateJson(@RequestBody String json) {
        boolean isValid = JsonUtils.isValidJson(json);
        return R.ok(isValid);
    }

    /**
     * JSON转Map
     *
     * @param json JSON字符串
     * @return Map对象
     */
    @PostMapping("/toMap")
    public R<Map<String, Object>> jsonToMap(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        Map<String, Object> map = JsonUtils.fromJsonToMap(json);
        return R.ok(map);
    }

    /**
     * 使用Jackson处理JSON
     *
     * @param json JSON字符串
     * @return 处理结果
     */
    @PostMapping("/jackson")
    public R<String> processWithJackson(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        JsonService jacksonService = JsonUtils.jackson();
        Object jsonObject = jacksonService.fromJson(json, Object.class);
        String prettyJson = jacksonService.toPrettyJson(jsonObject);
        return R.ok(prettyJson);
    }

    /**
     * 使用Gson处理JSON
     *
     * @param json JSON字符串
     * @return 处理结果
     */
    @PostMapping("/gson")
    public R<String> processWithGson(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        JsonService gsonService = JsonUtils.gson();
        Object jsonObject = gsonService.fromJson(json, Object.class);
        String prettyJson = gsonService.toPrettyJson(jsonObject);
        return R.ok(prettyJson);
    }

    /**
     * 使用Fastjson处理JSON
     *
     * @param json JSON字符串
     * @return 处理结果
     */
    @PostMapping("/fastjson")
    public R<String> processWithFastjson(@RequestBody String json) {
        if (!JsonUtils.isValidJson(json)) {
            return R.fail("无效的JSON格式");
        }
        JsonService fastjsonService = JsonUtils.fastjson();
        Object jsonObject = fastjsonService.fromJson(json, Object.class);
        String prettyJson = fastjsonService.toPrettyJson(jsonObject);
        return R.ok(prettyJson);
    }
}