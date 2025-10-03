package org.aimindflow.common.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.AliyunOssService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 阿里云OSS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class AliyunOssServiceImpl extends AbstractOssServiceImpl implements AliyunOssService {

    /**
     * 阿里云OSS客户端
     */
    private final OSS ossClient;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public AliyunOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        // 创建阿里云OSS客户端
        this.ossClient = new OSSClientBuilder().build(
                ossProperties.getAliyun().getEndpoint(),
                ossProperties.getAliyun().getAccessKey(),
                ossProperties.getAliyun().getSecretKey());
        // 初始化存储桶
        initBucket();
    }

    /**
     * 初始化存储桶
     */
    private void initBucket() {
        try {
            // 判断存储桶是否存在
            if (!bucketExists(ossProperties.getBucketName())) {
                // 创建存储桶
                createBucket(ossProperties.getBucketName());
            }
        } catch (Exception e) {
            log.error("初始化阿里云OSS存储桶失败", e);
            throw new OssException("初始化阿里云OSS存储桶失败", e);
        }
    }

    /**
     * 获取阿里云OSS客户端
     *
     * @return 阿里云OSS客户端
     */
    @Override
    public OSS getOssClient() {
        return ossClient;
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void createBucket(String bucketName) {
        try {
            if (!bucketExists(bucketName)) {
                ossClient.createBucket(bucketName);
            }
        } catch (Exception e) {
            log.error("创建阿里云OSS存储桶失败", e);
            throw new OssException("创建阿里云OSS存储桶失败", e);
        }
    }

    /**
     * 判断存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return 是否存在
     */
    @Override
    public boolean bucketExists(String bucketName) {
        try {
            return ossClient.doesBucketExist(bucketName);
        } catch (Exception e) {
            log.error("判断阿里云OSS存储桶是否存在失败", e);
            throw new OssException("判断阿里云OSS存储桶是否存在失败", e);
        }
    }

    /**
     * 删除存储桶
     *
     * @param bucketName 存储桶名称
     */
    @Override
    public void removeBucket(String bucketName) {
        try {
            if (bucketExists(bucketName)) {
                ossClient.deleteBucket(bucketName);
            }
        } catch (Exception e) {
            log.error("删除阿里云OSS存储桶失败", e);
            throw new OssException("删除阿里云OSS存储桶失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param inputStream 输入流
     * @param bucketName  存储桶名称
     * @param fileName    文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(InputStream inputStream, String bucketName, String fileName) {
        try {
            // 生成文件名
            String objectName = generateFileName(fileName);
            // 获取内容类型
            String contentType = getContentType(fileName);
            // 创建元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            // 上传文件
            ossClient.putObject(bucketName, objectName, inputStream, metadata);
            // 获取文件URL
            String url = getUrl(bucketName, objectName);
            // 获取文件大小
            long size = getSize(bucketName, objectName);
            // 创建OSS文件对象
            return createOssFile(objectName, fileName, url, size, contentType, bucketName);
        } catch (Exception e) {
            log.error("上传文件到阿里云OSS失败", e);
            throw new OssException(OssConstants.ERROR_CODE_UPLOAD_FAILED, OssConstants.ERROR_MESSAGE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     */
    @Override
    public void delete(String bucketName, String fileName) {
        try {
            ossClient.deleteObject(bucketName, fileName);
        } catch (Exception e) {
            log.error("从阿里云OSS删除文件失败", e);
            throw new OssException(OssConstants.ERROR_CODE_DELETE_FAILED, OssConstants.ERROR_MESSAGE_DELETE_FAILED, e);
        }
    }

    /**
     * 下载文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件字节数组
     */
    @Override
    public byte[] download(String bucketName, String fileName) {
        try {
            OSSObject ossObject = ossClient.getObject(bucketName, fileName);
            try (InputStream inputStream = ossObject.getObjectContent();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            log.error("从阿里云OSS下载文件失败", e);
            throw new OssException(OssConstants.ERROR_CODE_DOWNLOAD_FAILED, OssConstants.ERROR_MESSAGE_DOWNLOAD_FAILED, e);
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    @Override
    public OssFile getFile(String bucketName, String fileName) {
        try {
            // 获取文件元数据
            ObjectMetadata metadata = ossClient.getObjectMetadata(bucketName, fileName);
            // 获取文件URL
            String url = getUrl(bucketName, fileName);
            // 创建OSS文件对象
            return createOssFile(fileName, fileName, url, metadata.getContentLength(), metadata.getContentType(), bucketName);
        } catch (Exception e) {
            log.error("从阿里云OSS获取文件信息失败", e);
            throw new OssException(OssConstants.ERROR_CODE_FILE_NOT_FOUND, OssConstants.ERROR_MESSAGE_FILE_NOT_FOUND, e);
        }
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     前缀
     * @return 文件列表
     */
    @Override
    public List<OssFile> listFiles(String bucketName, String prefix) {
        try {
            List<OssFile> fileList = new ArrayList<>();
            // 列出对象
            ObjectListing objectListing = ossClient.listObjects(bucketName, prefix);
            // 遍历对象
            for (com.aliyun.oss.model.OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                // 获取文件URL
                String url = getUrl(bucketName, objectSummary.getKey());
                // 创建OSS文件对象
                OssFile ossFile = createOssFile(objectSummary.getKey(), objectSummary.getKey(), url, objectSummary.getSize(),
                        getContentType(objectSummary.getKey()), bucketName);
                fileList.add(ossFile);
            }
            return fileList;
        } catch (Exception e) {
            log.error("从阿里云OSS获取文件列表失败", e);
            throw new OssException(OssConstants.ERROR_CODE_LIST_FAILED, OssConstants.ERROR_MESSAGE_LIST_FAILED, e);
        }
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @param expires    过期时间（秒）
     * @return 文件URL
     */
    @Override
    public String getUrl(String bucketName, String fileName, int expires) {
        try {
            // 计算过期时间
            Date expiration = new Date(System.currentTimeMillis() + expires * 1000L);
            // 生成URL
            URL url = ossClient.generatePresignedUrl(bucketName, fileName, expiration);
            return url.toString();
        } catch (Exception e) {
            log.error("从阿里云OSS获取文件URL失败", e);
            throw new OssException(OssConstants.ERROR_CODE_GET_URL_FAILED, OssConstants.ERROR_MESSAGE_GET_URL_FAILED, e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 是否存在
     */
    @Override
    public boolean exists(String bucketName, String fileName) {
        try {
            return ossClient.doesObjectExist(bucketName, fileName);
        } catch (Exception e) {
            log.error("判断阿里云OSS文件是否存在失败", e);
            throw new OssException("判断阿里云OSS文件是否存在失败", e);
        }
    }

    /**
     * 获取文件大小
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件大小（字节）
     */
    @Override
    public long getSize(String bucketName, String fileName) {
        try {
            ObjectMetadata metadata = ossClient.getObjectMetadata(bucketName, fileName);
            return metadata.getContentLength();
        } catch (Exception e) {
            log.error("获取阿里云OSS文件大小失败", e);
            throw new OssException("获取阿里云OSS文件大小失败", e);
        }
    }
}