package org.aimindflow.common.oss.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.MinioOssService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * MinIO OSS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class MinioOssServiceImpl extends AbstractOssServiceImpl implements MinioOssService {

    /**
     * MinIO客户端
     */
    private final MinioClient minioClient;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public MinioOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        // 创建MinIO客户端
        this.minioClient = MinioClient.builder()
                .endpoint(ossProperties.getMinio().getEndpoint())
                .credentials(ossProperties.getMinio().getAccessKey(), ossProperties.getMinio().getSecretKey())
                .build();
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
            log.error("初始化MinIO存储桶失败", e);
            throw new OssException("初始化MinIO存储桶失败", e);
        }
    }

    /**
     * 获取MinIO客户端
     *
     * @return MinIO客户端
     */
    @Override
    public MinioClient getMinioClient() {
        return minioClient;
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
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("创建MinIO存储桶失败", e);
            throw new OssException("创建MinIO存储桶失败", e);
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
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("判断MinIO存储桶是否存在失败", e);
            throw new OssException("判断MinIO存储桶是否存在失败", e);
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
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("删除MinIO存储桶失败", e);
            throw new OssException("删除MinIO存储桶失败", e);
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
            // 上传文件
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .contentType(contentType)
                    .stream(inputStream, inputStream.available(), -1)
                    .build());
            // 获取文件URL
            String url = getUrl(bucketName, objectName);
            // 获取文件大小
            long size = getSize(bucketName, objectName);
            // 创建OSS文件对象
            return createOssFile(objectName, fileName, url, size, contentType, bucketName);
        } catch (Exception e) {
            log.error("上传文件到MinIO失败", e);
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
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO删除文件失败", e);
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
        try (InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } catch (Exception e) {
            log.error("从MinIO下载文件失败", e);
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
            // 获取文件信息
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            // 获取文件URL
            String url = getUrl(bucketName, fileName);
            // 创建OSS文件对象
            return createOssFile(fileName, fileName, url, stat.size(), stat.contentType(), bucketName);
        } catch (Exception e) {
            log.error("从MinIO获取文件信息失败", e);
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
            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .recursive(true)
                    .build());
            // 遍历对象
            for (Result<Item> result : results) {
                Item item = result.get();
                // 获取文件URL
                String url = getUrl(bucketName, item.objectName());
                // 创建OSS文件对象
                OssFile ossFile = createOssFile(item.objectName(), item.objectName(), url, item.size(),
                        getContentType(item.objectName()), bucketName);
                fileList.add(ossFile);
            }
            return fileList;
        } catch (Exception e) {
            log.error("从MinIO获取文件列表失败", e);
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
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .method(Method.GET)
                    .expiry(expires, TimeUnit.SECONDS)
                    .build());
        } catch (Exception e) {
            log.error("从MinIO获取文件URL失败", e);
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
            minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return true;
        } catch (ErrorResponseException e) {
            if (e.errorResponse().code().equals("NoSuchKey")) {
                return false;
            }
            log.error("判断MinIO文件是否存在失败", e);
            throw new OssException("判断MinIO文件是否存在失败", e);
        } catch (Exception e) {
            log.error("判断MinIO文件是否存在失败", e);
            throw new OssException("判断MinIO文件是否存在失败", e);
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
            StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            return stat.size();
        } catch (Exception e) {
            log.error("获取MinIO文件大小失败", e);
            throw new OssException("获取MinIO文件大小失败", e);
        }
    }
}