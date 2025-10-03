package org.aimindflow.common.oss.service;

import com.aliyun.oss.OSS;

/**
 * 阿里云OSS服务接口
 *
 * @author HezaoHezao
 */
public interface AliyunOssService extends OssService {

    /**
     * 获取阿里云OSS客户端
     *
     * @return 阿里云OSS客户端
     */
    OSS getOssClient();

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    void createBucket(String bucketName);

    /**
     * 判断存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    boolean bucketExists(String bucketName);

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     */
    void removeBucket(String bucketName);
}