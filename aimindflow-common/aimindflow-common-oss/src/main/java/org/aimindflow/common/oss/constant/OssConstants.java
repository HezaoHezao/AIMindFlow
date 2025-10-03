package org.aimindflow.common.oss.constant;

/**
 * OSS常量
 *
 * @author HezaoHezao
 */
public class OssConstants {

    /**
     * 存储类型
     */
    public static final String STORAGE_TYPE_MINIO = "minio";
    public static final String STORAGE_TYPE_ALIYUN = "aliyun";
    public static final String STORAGE_TYPE_QINIU = "qiniu";
    public static final String STORAGE_TYPE_TENCENT = "tencent";
    public static final String STORAGE_TYPE_HUAWEI = "huawei";
    public static final String STORAGE_TYPE_AWS = "aws";
    public static final String STORAGE_TYPE_LOCAL = "local";

    /**
     * 默认过期时间（秒）
     */
    public static final int DEFAULT_EXPIRES = 3600;

    /**
     * 默认分片大小（5MB）
     */
    public static final int DEFAULT_PART_SIZE = 5 * 1024 * 1024;

    /**
     * 默认最大分片数
     */
    public static final int DEFAULT_MAX_PARTS = 10000;

    /**
     * 默认并发数
     */
    public static final int DEFAULT_TASK_NUM = 5;

    /**
     * 默认存储桶名称
     */
    public static final String DEFAULT_BUCKET_NAME = "default";

    /**
     * 默认存储路径
     */
    public static final String DEFAULT_BASE_PATH = "";

    /**
     * 默认域名
     */
    public static final String DEFAULT_DOMAIN = "";

    /**
     * 默认地域
     */
    public static final String DEFAULT_REGION = "";

    /**
     * 错误码
     */
    public static final String ERROR_CODE_UPLOAD_FAILED = "OSS-10001";
    public static final String ERROR_CODE_DOWNLOAD_FAILED = "OSS-10002";
    public static final String ERROR_CODE_DELETE_FAILED = "OSS-10003";
    public static final String ERROR_CODE_LIST_FAILED = "OSS-10004";
    public static final String ERROR_CODE_GET_URL_FAILED = "OSS-10005";
    public static final String ERROR_CODE_FILE_NOT_FOUND = "OSS-10006";
    public static final String ERROR_CODE_INVALID_PARAM = "OSS-10007";
    public static final String ERROR_CODE_BUCKET_NOT_FOUND = "OSS-10008";
    public static final String ERROR_CODE_STORAGE_TYPE_NOT_SUPPORTED = "OSS-10009";

    /**
     * 错误消息
     */
    public static final String ERROR_MESSAGE_UPLOAD_FAILED = "文件上传失败";
    public static final String ERROR_MESSAGE_DOWNLOAD_FAILED = "文件下载失败";
    public static final String ERROR_MESSAGE_DELETE_FAILED = "文件删除失败";
    public static final String ERROR_MESSAGE_LIST_FAILED = "获取文件列表失败";
    public static final String ERROR_MESSAGE_GET_URL_FAILED = "获取文件URL失败";
    public static final String ERROR_MESSAGE_FILE_NOT_FOUND = "文件不存在";
    public static final String ERROR_MESSAGE_INVALID_PARAM = "参数无效";
    public static final String ERROR_MESSAGE_BUCKET_NOT_FOUND = "存储桶不存在";
    public static final String ERROR_MESSAGE_STORAGE_TYPE_NOT_SUPPORTED = "存储类型不支持";
}