package org.aimindflow.common.core.utils.uuid;

import java.util.UUID;

/**
 * ID工具类
 */
public class IdUtils {

    /**
     * 生成无分隔符的UUID字符串
     */
    public static String fastSimpleUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}