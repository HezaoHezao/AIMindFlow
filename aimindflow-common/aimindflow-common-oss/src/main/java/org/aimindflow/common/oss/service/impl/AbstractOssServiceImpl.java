package org.aimindflow.common.oss.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.utils.DateUtils;
import org.aimindflow.common.core.utils.StringUtils;
import org.aimindflow.common.core.utils.file.FileTypeUtils;
import org.aimindflow.common.core.utils.file.FileUtils;
import org.aimindflow.common.core.utils.uuid.IdUtils;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.OssService;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 抽象OSS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public abstract class AbstractOssServiceImpl implements OssService {

    /**
     * OSS配置属性
     */
    protected final OssProperties ossProperties;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public AbstractOssServiceImpl(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 文件信息
     */
    @Override
    public OssFile upload(File file) {
        return upload(file, file.getName());
    }

    /**
     * 上传文件
     *
     * @param file     文件
     * @param fileName 文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(File file, String fileName) {
        return upload(file, ossProperties.getBucketName(), fileName);
    }

    /**
     * 上传文件
     *
     * @param file       文件
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(File file, String bucketName, String fileName) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            return upload(inputStream, bucketName, fileName);
        } catch (IOException e) {
            throw new OssException(OssConstants.ERROR_CODE_UPLOAD_FAILED, OssConstants.ERROR_MESSAGE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param fileName    文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(InputStream inputStream, String fileName) {
        return upload(inputStream, ossProperties.getBucketName(), fileName);
    }

    /**
     * 上传文件
     *
     * @param data     字节数组
     * @param fileName 文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(byte[] data, String fileName) {
        return upload(data, ossProperties.getBucketName(), fileName);
    }

    /**
     * 上传文件
     *
     * @param data       字节数组
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(byte[] data, String bucketName, String fileName) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
            return upload(inputStream, bucketName, fileName);
        } catch (IOException e) {
            throw new OssException(OssConstants.ERROR_CODE_UPLOAD_FAILED, OssConstants.ERROR_MESSAGE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    @Override
    public void delete(String fileName) {
        delete(ossProperties.getBucketName(), fileName);
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     */
    @Override
    public void delete(List<String> fileNames) {
        delete(ossProperties.getBucketName(), fileNames);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  文件名列表
     */
    @Override
    public void delete(String bucketName, List<String> fileNames) {
        for (String fileName : fileNames) {
            delete(bucketName, fileName);
        }
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @return 文件字节数组
     */
    @Override
    public byte[] download(String fileName) {
        return download(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件
     *
     * @param fileName 文件名
     * @return 文件信息
     */
    @Override
    public OssFile getFile(String fileName) {
        return getFile(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件列表
     *
     * @return 文件列表
     */
    @Override
    public List<OssFile> listFiles() {
        return listFiles(ossProperties.getBucketName());
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储桶名称
     * @return 文件列表
     */
    @Override
    public List<OssFile> listFiles(String bucketName) {
        return listFiles(bucketName, "");
    }

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    @Override
    public String getUrl(String fileName) {
        return getUrl(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件URL
     */
    @Override
    public String getUrl(String bucketName, String fileName) {
        return getUrl(bucketName, fileName, ossProperties.getExpires());
    }

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @param expires  过期时间（秒）
     * @return 文件URL
     */
    @Override
    public String getUrl(String fileName, int expires) {
        return getUrl(ossProperties.getBucketName(), fileName, expires);
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    @Override
    public boolean exists(String fileName) {
        return exists(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件大小
     *
     * @param fileName 文件名
     * @return 文件大小（字节）
     */
    @Override
    public long getSize(String fileName) {
        return getSize(ossProperties.getBucketName(), fileName);
    }

    /**
     * 生成文件名
     *
     * @param originalFileName 原始文件名
     * @return 生成的文件名
     */
    protected String generateFileName(String originalFileName) {
        // 获取文件后缀
        String extension = FileUtils.getExtension(originalFileName);
        // 生成文件名
        String fileName = DateUtils.datePath() + "/" + IdUtils.fastSimpleUUID();
        // 添加后缀
        if (StringUtils.isNotEmpty(extension)) {
            fileName += "." + extension;
        }
        // 添加基础路径
        if (StringUtils.isNotEmpty(ossProperties.getBasePath())) {
            fileName = ossProperties.getBasePath() + "/" + fileName;
        }
        return fileName;
    }

    /**
     * 创建OSS文件对象
     *
     * @param fileName         文件名
     * @param originalFileName 原始文件名
     * @param url              文件URL
     * @param size             文件大小
     * @param contentType      内容类型
     * @param bucketName       存储桶名称
     * @return OSS文件对象
     */
    protected OssFile createOssFile(String fileName, String originalFileName, String url, long size,
                                    String contentType, String bucketName) {
        OssFile ossFile = new OssFile();
        ossFile.setFileName(fileName);
        ossFile.setOriginalFileName(originalFileName);
        ossFile.setUrl(url);
        ossFile.setSize(size);
        ossFile.setContentType(contentType);
        ossFile.setBucketName(bucketName);
        ossFile.setPath(fileName);
        // 设置创建时间（OssFile不包含uploadTime字段）
        ossFile.setCreateTime(DateUtils.getTime());
        ossFile.setStorageType(getStorageType());
        return ossFile;
    }

    /**
     * 获取内容类型
     *
     * @param fileName 文件名
     * @return 内容类型
     */
    protected String getContentType(String fileName) {
        return FileTypeUtils.getContentType(fileName);
    }

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    @Override
    public String getStorageType() {
        return ossProperties.getStorageType();
    }
}