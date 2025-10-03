package org.aimindflow.common.oss.service;

import com.qcloud.cos.COSClient;

/**
 * 腾讯云COS服务接口
 *
 * @author HezaoHezao
 */
public interface TencentOssService extends OssService {

    /**
     * 获取腾讯云COS客户端
     *
     * @return 腾讯云COS客户端
     */
    COSClient getCosClient();

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