package org.aimindflow.common.oss.service;

import io.minio.MinioClient;

/**
 * MinIO OSS服务接口
 *
 * @author HezaoHezao
 */
public interface MinioOssService extends OssService {

    /**
     * 获取MinIO客户端
     *
     * @return MinIO客户端
     */
    MinioClient getMinioClient();

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