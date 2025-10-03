package org.aimindflow.common.oss.config;

import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.service.*;
import org.aimindflow.common.oss.service.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * OSS配置类
 *
 * @author HezaoHezao
 */
@Configuration
@EnableConfigurationProperties(OssProperties.class)
@ConditionalOnProperty(prefix = "oss", name = "enabled", havingValue = "true", matchIfMissing = true)
public class OssConfig {

    /**
     * 创建OSS服务
     *
     * @param ossProperties OSS配置属性
     * @return OSS服务
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(OssService.class)
    public OssService ossService(OssProperties ossProperties) {
        String storageType = ossProperties.getStorageType();
        switch (storageType) {
            case OssConstants.STORAGE_TYPE_MINIO:
                return new MinioOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_ALIYUN:
                return new AliyunOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_QINIU:
                return new QiniuOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_TENCENT:
                return new TencentOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_HUAWEI:
                return new HuaweiOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_AWS:
                return new AwsOssServiceImpl(ossProperties);
            case OssConstants.STORAGE_TYPE_LOCAL:
                return new LocalOssServiceImpl(ossProperties);
            default:
                throw new OssException(OssConstants.ERROR_CODE_STORAGE_TYPE_NOT_SUPPORTED,
                        OssConstants.ERROR_MESSAGE_STORAGE_TYPE_NOT_SUPPORTED + ": " + storageType);
        }
    }

    /**
     * 创建MinIO OSS服务
     *
     * @param ossProperties OSS配置属性
     * @return MinIO OSS服务
     */
    @Bean
    @ConditionalOnMissingBean(MinioOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_MINIO)
    public MinioOssService minioOssService(OssProperties ossProperties) {
        return new MinioOssServiceImpl(ossProperties);
    }

    /**
     * 创建阿里云OSS服务
     *
     * @param ossProperties OSS配置属性
     * @return 阿里云OSS服务
     */
    @Bean
    @ConditionalOnMissingBean(AliyunOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_ALIYUN)
    public AliyunOssService aliyunOssService(OssProperties ossProperties) {
        return new AliyunOssServiceImpl(ossProperties);
    }

    /**
     * 创建七牛云OSS服务
     *
     * @param ossProperties OSS配置属性
     * @return 七牛云OSS服务
     */
    @Bean
    @ConditionalOnMissingBean(QiniuOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_QINIU)
    public QiniuOssService qiniuOssService(OssProperties ossProperties) {
        return new QiniuOssServiceImpl(ossProperties);
    }

    /**
     * 创建腾讯云COS服务
     *
     * @param ossProperties OSS配置属性
     * @return 腾讯云COS服务
     */
    @Bean
    @ConditionalOnMissingBean(TencentOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_TENCENT)
    public TencentOssService tencentOssService(OssProperties ossProperties) {
        return new TencentOssServiceImpl(ossProperties);
    }

    /**
     * 创建华为云OBS服务
     *
     * @param ossProperties OSS配置属性
     * @return 华为云OBS服务
     */
    @Bean
    @ConditionalOnMissingBean(HuaweiOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_HUAWEI)
    public HuaweiOssService huaweiOssService(OssProperties ossProperties) {
        return new HuaweiOssServiceImpl(ossProperties);
    }

    /**
     * 创建AWS S3服务
     *
     * @param ossProperties OSS配置属性
     * @return AWS S3服务
     */
    @Bean
    @ConditionalOnMissingBean(AwsOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_AWS)
    public AwsOssService awsOssService(OssProperties ossProperties) {
        return new AwsOssServiceImpl(ossProperties);
    }

    /**
     * 创建本地存储服务
     *
     * @param ossProperties OSS配置属性
     * @return 本地存储服务
     */
    @Bean
    @ConditionalOnMissingBean(LocalOssService.class)
    @ConditionalOnProperty(prefix = "oss", name = "storage-type", havingValue = OssConstants.STORAGE_TYPE_LOCAL)
    public LocalOssService localOssService(OssProperties ossProperties) {
        return new LocalOssServiceImpl(ossProperties);
    }
}