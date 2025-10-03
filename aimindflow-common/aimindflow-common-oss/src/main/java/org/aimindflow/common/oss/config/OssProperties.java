package org.aimindflow.common.oss.config;

import lombok.Data;
import org.aimindflow.common.oss.constant.OssConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OSS配置属性
 *
 * @author HezaoHezao
 */
@Data
@ConfigurationProperties(prefix = "oss")
public class OssProperties {

    /**
     * 存储类型
     */
    private String storageType = OssConstants.STORAGE_TYPE_MINIO;

    /**
     * 是否启用
     */
    private boolean enabled = true;

    /**
     * 存储桶名称
     */
    private String bucketName = OssConstants.DEFAULT_BUCKET_NAME;

    /**
     * 存储路径
     */
    private String basePath = OssConstants.DEFAULT_BASE_PATH;

    /**
     * 域名
     */
    private String domain = OssConstants.DEFAULT_DOMAIN;

    /**
     * 地域
     */
    private String region = OssConstants.DEFAULT_REGION;

    /**
     * 过期时间（秒）
     */
    private int expires = OssConstants.DEFAULT_EXPIRES;

    /**
     * 分片大小（字节）
     */
    private int partSize = OssConstants.DEFAULT_PART_SIZE;

    /**
     * 最大分片数
     */
    private int maxParts = OssConstants.DEFAULT_MAX_PARTS;

    /**
     * 并发数
     */
    private int taskNum = OssConstants.DEFAULT_TASK_NUM;

    /**
     * 本地存储配置
     */
    private Local local = new Local();

    /**
     * MinIO配置
     */
    private Minio minio = new Minio();

    /**
     * 阿里云OSS配置
     */
    private Aliyun aliyun = new Aliyun();

    /**
     * 七牛云配置
     */
    private Qiniu qiniu = new Qiniu();

    /**
     * 腾讯云COS配置
     */
    private Tencent tencent = new Tencent();

    /**
     * 华为云OBS配置
     */
    private Huawei huawei = new Huawei();

    /**
     * AWS S3配置
     */
    private Aws aws = new Aws();

    /**
     * 本地存储配置
     */
    @Data
    public static class Local {
        /**
         * 存储路径
         */
        private String storagePath = "upload";

        /**
         * 访问路径
         */
        private String accessPath = "/upload/**";

        /**
         * 访问前缀
         */
        private String accessPrefix = "/upload";
    }

    /**
     * MinIO配置
     */
    @Data
    public static class Minio {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;

        /**
         * 是否启用HTTPS
         */
        private boolean secure = false;
    }

    /**
     * 阿里云OSS配置
     */
    @Data
    public static class Aliyun {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;
    }

    /**
     * 七牛云配置
     */
    @Data
    public static class Qiniu {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;
    }

    /**
     * 腾讯云COS配置
     */
    @Data
    public static class Tencent {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;

        /**
         * 应用ID
         */
        private String appId;
    }

    /**
     * 华为云OBS配置
     */
    @Data
    public static class Huawei {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;
    }

    /**
     * AWS S3配置
     */
    @Data
    public static class Aws {
        /**
         * 服务地址
         */
        private String endpoint;

        /**
         * 访问密钥
         */
        private String accessKey;

        /**
         * 密钥
         */
        private String secretKey;
    }
}