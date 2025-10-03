package org.aimindflow.common.oss.service;

import software.amazon.awssdk.services.s3.S3Client;

/**
 * AWS S3服务接口
 *
 * @author HezaoHezao
 */
public interface AwsOssService extends OssService {

    /**
     * 获取AWS S3客户端
     *
     * @return AWS S3客户端
     */
    S3Client getS3Client();

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