package org.aimindflow.common.oss.service;

import com.obs.services.ObsClient;

/**
 * 华为云OBS服务接口
 *
 * @author HezaoHezao
 */
public interface HuaweiOssService extends OssService {

    /**
     * 获取华为云OBS客户端
     *
     * @return 华为云OBS客户端
     */
    ObsClient getObsClient();

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