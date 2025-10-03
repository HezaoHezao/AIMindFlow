package org.aimindflow.common.oss.service;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;

/**
 * 七牛云OSS服务接口
 *
 * @author HezaoHezao
 */
public interface QiniuOssService extends OssService {

    /**
     * 获取七牛云上传管理器
     *
     * @return 七牛云上传管理器
     */
    UploadManager getUploadManager();

    /**
     * 获取七牛云存储桶管理器
     *
     * @return 七牛云存储桶管理器
     */
    BucketManager getBucketManager();

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