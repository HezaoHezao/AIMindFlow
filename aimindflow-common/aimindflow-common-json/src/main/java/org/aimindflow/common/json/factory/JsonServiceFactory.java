package org.aimindflow.common.json.factory;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.json.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * JSON服务工厂
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class JsonServiceFactory {

    private static JsonService jacksonJsonService;
    private static JsonService gsonJsonService;
    private static JsonService fastjsonJsonService;

    @Autowired
    public JsonServiceFactory(
            @Qualifier("jacksonJsonService") JsonService jacksonJsonService,
            @Qualifier("gsonJsonService") JsonService gsonJsonService,
            @Qualifier("fastjsonJsonService") JsonService fastjsonJsonService) {
        JsonServiceFactory.jacksonJsonService = jacksonJsonService;
        JsonServiceFactory.gsonJsonService = gsonJsonService;
        JsonServiceFactory.fastjsonJsonService = fastjsonJsonService;
    }

    /**
     * 获取默认JSON服务（Jackson）
     *
     * @return JSON服务
     */
    public static JsonService getJsonService() {
        return jacksonJsonService;
    }

    /**
     * 获取Jackson JSON服务
     *
     * @return Jackson JSON服务
     */
    public static JsonService getJacksonJsonService() {
        return jacksonJsonService;
    }

    /**
     * 获取Gson JSON服务
     *
     * @return Gson JSON服务
     */
    public static JsonService getGsonJsonService() {
        return gsonJsonService;
    }

    /**
     * 获取Fastjson JSON服务
     *
     * @return Fastjson JSON服务
     */
    public static JsonService getFastjsonJsonService() {
        return fastjsonJsonService;
    }
}