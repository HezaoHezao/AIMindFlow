package org.aimindflow.common.core.utils.file;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 文件类型工具类
 */
public class FileTypeUtils {

    private static final Map<String, String> MIME_TYPES = new HashMap<>();

    static {
        MIME_TYPES.put("png", "image/png");
        MIME_TYPES.put("jpg", "image/jpeg");
        MIME_TYPES.put("jpeg", "image/jpeg");
        MIME_TYPES.put("gif", "image/gif");
        MIME_TYPES.put("svg", "image/svg+xml");
        MIME_TYPES.put("webp", "image/webp");
        MIME_TYPES.put("bmp", "image/bmp");
        MIME_TYPES.put("pdf", "application/pdf");
        MIME_TYPES.put("txt", "text/plain");
        MIME_TYPES.put("csv", "text/csv");
        MIME_TYPES.put("json", "application/json");
        MIME_TYPES.put("xml", "application/xml");
        MIME_TYPES.put("zip", "application/zip");
        MIME_TYPES.put("gz", "application/gzip");
        MIME_TYPES.put("tar", "application/x-tar");
        MIME_TYPES.put("mp4", "video/mp4");
        MIME_TYPES.put("mp3", "audio/mpeg");
        MIME_TYPES.put("wav", "audio/wav");
        MIME_TYPES.put("doc", "application/msword");
        MIME_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        MIME_TYPES.put("xls", "application/vnd.ms-excel");
        MIME_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        MIME_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        MIME_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    }

    /**
     * 根据文件名获取内容类型
     */
    public static String getContentType(String filename) {
        if (filename == null) {
            return "application/octet-stream";
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0 || lastDot == filename.length() - 1) {
            return "application/octet-stream";
        }
        String ext = filename.substring(lastDot + 1).toLowerCase(Locale.ROOT);
        return MIME_TYPES.getOrDefault(ext, "application/octet-stream");
    }
}