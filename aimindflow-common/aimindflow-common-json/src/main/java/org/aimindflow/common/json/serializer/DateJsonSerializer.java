package org.aimindflow.common.json.serializer;

import org.aimindflow.common.json.constant.JsonConstants;
import org.aimindflow.common.json.exception.JsonException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期JSON序列化器
 *
 * @author HezaoHezao
 */
public class DateJsonSerializer implements JsonSerializer<Date> {

    private final String dateFormat;

    /**
     * 构造函数
     */
    public DateJsonSerializer() {
        this(JsonConstants.DEFAULT_DATE_FORMAT);
    }

    /**
     * 构造函数
     *
     * @param dateFormat 日期格式
     */
    public DateJsonSerializer(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public String serialize(Date date) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return '"' + sdf.format(date) + '"';
    }

    @Override
    public Date deserialize(String json) {
        if (json == null || json.isEmpty() || "null".equals(json)) {
            return null;
        }
        // 去除JSON字符串中的引号
        String dateStr = json.replace('"', ' ').trim();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new JsonException("日期反序列化失败: " + dateStr, e);
        }
    }
}