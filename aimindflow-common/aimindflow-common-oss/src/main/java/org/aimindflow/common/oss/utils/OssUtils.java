package org.aimindflow.common.oss.utils;

import org.aimindflow.common.core.utils.SpringUtils;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.OssService;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * OSS工具类
 *
 * @author HezaoHezao
 */
public class OssUtils {

    /**
     * 获取OSS服务
     *
     * @return OSS服务
     */
    public static OssService getOssService() {
        return SpringUtils.getBean(OssService.class);
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    public static OssFile upload(File file) {
        return getOssService().upload(file);
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名
     * @return 文件信息
     */
    public static OssFile upload(File file, String fileName) {
        return getOssService().upload(file, fileName);
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    public static OssFile upload(File file, String bucketName, String fileName) {
        return getOssService().upload(file, bucketName, fileName);
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 文件信息
     */
    public static OssFile upload(InputStream inputStream, String fileName) {
        return getOssService().upload(inputStream, fileName);
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param bucketName  存储桶名称
     * @param fileName    文件名
     * @return 文件信息
     */
    public static OssFile upload(InputStream inputStream, String bucketName, String fileName) {
        return getOssService().upload(inputStream, bucketName, fileName);
    }

    /**
     * 上传文件
     *
     * @param data     字节数组
     * @param fileName 文件名
     * @return 文件信息
     */
    public static OssFile upload(byte[] data, String fileName) {
        return getOssService().upload(data, fileName);
    }

    /**
     * 上传文件
     *
     * @param data       字节数组
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    public static OssFile upload(byte[] data, String bucketName, String fileName) {
        return getOssService().upload(data, bucketName, fileName);
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    public static void delete(String fileName) {
        getOssService().delete(fileName);
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     */
    public static void delete(String bucketName, String fileName) {
        getOssService().delete(bucketName, fileName);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     */
    public static void delete(List<String> fileNames) {
        getOssService().delete(fileNames);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  文件名列表
     */
    public static void delete(String bucketName, List<String> fileNames) {
        getOssService().delete(bucketName, fileNames);
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @return 文件字节数组
     */
    public static byte[] download(String fileName) {
        return getOssService().download(fileName);
    }

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件字节数组
     */
    public static byte[] download(String bucketName, String fileName) {
        return getOssService().download(bucketName, fileName);
    }

    /**
     * 获取文件
     *
     * @param fileName 文件名
     * @return 文件信息
     */
    public static OssFile getFile(String fileName) {
        return getOssService().getFile(fileName);
    }

    /**
     * 获取文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    public static OssFile getFile(String bucketName, String fileName) {
        return getOssService().getFile(bucketName, fileName);
    }

    /**
     * 获取文件列表
     *
     * @return 文件列表
     */
    public static List<OssFile> listFiles() {
        return getOssService().listFiles();
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储桶名称
     * @return 文件列表
     */
    public static List<OssFile> listFiles(String bucketName) {
        return getOssService().listFiles(bucketName);
    }

    /**
     * 获取文件列表
     *
     * @param prefix 前缀
     * @return 文件列表
     */
    public static List<OssFile> listFiles(String bucketName, String prefix) {
        return getOssService().listFiles(bucketName, prefix);
    }

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    public static String getUrl(String fileName) {
        return getOssService().getUrl(fileName);
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件URL
     */
    public static String getUrl(String bucketName, String fileName) {
        return getOssService().getUrl(bucketName, fileName);
    }

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @param expires  过期时间（秒）
     * @return 文件URL
     */
    public static String getUrl(String fileName, int expires) {
        return getOssService().getUrl(fileName, expires);
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @param expires    过期时间（秒）
     * @return 文件URL
     */
    public static String getUrl(String bucketName, String fileName, int expires) {
        return getOssService().getUrl(bucketName, fileName, expires);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    public static boolean exists(String fileName) {
        return getOssService().exists(fileName);
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 是否存在
     */
    public static boolean exists(String bucketName, String fileName) {
        return getOssService().exists(bucketName, fileName);
    }

    /**
     * 获取文件大小
     *
     * @param fileName 文件名
     * @return 文件大小（字节）
     */
    public static long getSize(String fileName) {
        return getOssService().getSize(fileName);
    }

    /**
     * 获取文件大小
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件大小（字节）
     */
    public static long getSize(String bucketName, String fileName) {
        return getOssService().getSize(bucketName, fileName);
    }

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    public static String getStorageType() {
        return getOssService().getStorageType();
    }
}