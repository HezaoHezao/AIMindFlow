package org.aimindflow.common.oss.service;

import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.model.OssResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * OSS服务接口
 *
 * @author HezaoHezao
 */
public interface OssService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    OssResult uploadFile(MultipartFile file);

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名
     * @return 上传结果
     */
    OssResult uploadFile(MultipartFile file, String fileName);

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 上传结果
     */
    OssResult uploadFile(InputStream inputStream, String fileName);

    /**
     * 上传文件
     *
     * @param data     文件数据
     * @param fileName 文件名
     * @return 上传结果
     */
    OssResult uploadFile(byte[] data, String fileName);

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return 删除结果
     */
    boolean deleteFile(String fileName);

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     * @return 删除结果
     */
    boolean deleteFiles(List<String> fileNames);

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @return 文件流
     */
    InputStream downloadFile(String fileName);

    /**
     * 获取文件
     *
     * @param fileName 文件名
     * @return 文件对象
     */
    OssFile getFile(String fileName);

    /**
     * 获取文件列表
     *
     * @param prefix 前缀
     * @return 文件列表
     */
    List<OssFile> listFiles(String prefix);

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    String getFileUrl(String fileName);

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @param expires  过期时间（秒）
     * @return 文件URL
     */
    String getFileUrl(String fileName, Integer expires);

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    boolean isFileExist(String fileName);

    /**
     * 获取文件大小
     *
     * @param fileName 文件名
     * @return 文件大小（字节）
     */
    long getFileSize(String fileName);

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    String getStorageType();
}