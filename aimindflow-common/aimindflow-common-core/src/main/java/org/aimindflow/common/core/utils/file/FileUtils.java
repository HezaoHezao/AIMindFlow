package org.aimindflow.common.core.utils.file;

import java.io.File;

/**
 * 文件工具类
 */
public class FileUtils {

    /**
     * 获取文件扩展名（不含点）
     */
    public static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastDot = filename.lastIndexOf('.');
        if (lastDot < 0 || lastDot == filename.length() - 1) {
            return null;
        }
        return filename.substring(lastDot + 1);
    }

    /**
     * 递归删除目录
     */
    public static void deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    // 删除文件
                    file.delete();
                }
            }
        }
        // 删除目录本身
        dir.delete();
    }
}