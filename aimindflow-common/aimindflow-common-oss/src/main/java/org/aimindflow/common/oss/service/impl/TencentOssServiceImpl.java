package org.aimindflow.common.oss.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.utils.StringUtils;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.TencentOssService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 腾讯云COS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class TencentOssServiceImpl extends AbstractOssServiceImpl implements TencentOssService {

    /**
     * 腾讯云COS客户端
     */
    private final COSClient cosClient;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public TencentOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        // 初始化腾讯云COS客户端
        COSCredentials cred = new BasicCOSCredentials(ossProperties.getTencent().getSecretId(), ossProperties.getTencent().getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(ossProperties.getRegion()));
        // 设置使用HTTPS协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        this.cosClient = new COSClient(cred, clientConfig);
        // 创建默认存储桶
        createBucket(ossProperties.getBucketName());
    }

    /**
     * 获取腾讯云COS客户端
     *
     * @return 腾讯云COS客户端
     */
    @Override
    public COSClient getCosClient() {
        return this.cosClient;
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
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                // 设置存储桶访问权限为私有
                createBucketRequest.setCannedAcl(CannedAccessControlList.Private);
                cosClient.createBucket(createBucketRequest);
                log.info("创建腾讯云COS存储桶成功: {}", bucketName);
            }
        } catch (CosServiceException e) {
            log.error("创建腾讯云COS存储桶失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_CREATE_BUCKET_FAILED, OssConstants.ERROR_MESSAGE_CREATE_BUCKET_FAILED, e);
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
            return cosClient.doesBucketExist(bucketName);
        } catch (CosClientException e) {
            log.error("检查腾讯云COS存储桶是否存在失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_CHECK_BUCKET_FAILED, OssConstants.ERROR_MESSAGE_CHECK_BUCKET_FAILED, e);
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
                cosClient.deleteBucket(bucketName);
                log.info("删除腾讯云COS存储桶成功: {}", bucketName);
            }
        } catch (CosServiceException e) {
            log.error("删除腾讯云COS存储桶失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_REMOVE_BUCKET_FAILED, OssConstants.ERROR_MESSAGE_REMOVE_BUCKET_FAILED, e);
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
            String objectName = generateObjectName(fileName);
            // 获取文件内容类型
            String contentType = getContentType(fileName);
            // 创建上传请求
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream, metadata);
            // 上传文件
            cosClient.putObject(putObjectRequest);
            // 获取文件URL
            String url = getFileUrl(bucketName, objectName);
            // 创建OSS文件对象
            return createOssFile(bucketName, objectName, fileName, url, contentType);
        } catch (CosServiceException e) {
            log.error("上传文件到腾讯云COS失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_UPLOAD_FAILED, OssConstants.ERROR_MESSAGE_UPLOAD_FAILED, e);
        }
    }

    /**
     * 上传文件
     *
     * @param data        字节数组
     * @param bucketName  存储桶名称
     * @param fileName    文件名
     * @return 文件信息
     */
    @Override
    public OssFile upload(byte[] data, String bucketName, String fileName) {
        return upload(new ByteArrayInputStream(data), bucketName, fileName);
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     */
    @Override
    public void delete(String fileName) {
        delete(ossProperties.getBucketName(), fileName);
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
            cosClient.deleteObject(bucketName, fileName);
        } catch (CosServiceException e) {
            log.error("从腾讯云COS删除文件失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_DELETE_FAILED, OssConstants.ERROR_MESSAGE_DELETE_FAILED, e);
        }
    }

    /**
     * 批量删除文件
     *
     * @param fileNames 文件名列表
     */
    @Override
    public void deleteBatch(List<String> fileNames) {
        deleteBatch(ossProperties.getBucketName(), fileNames);
    }

    /**
     * 批量删除文件
     *
     * @param bucketName 存储桶名称
     * @param fileNames  文件名列表
     */
    @Override
    public void deleteBatch(String bucketName, List<String> fileNames) {
        try {
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
            List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
            for (String fileName : fileNames) {
                keys.add(new DeleteObjectsRequest.KeyVersion(fileName));
            }
            deleteObjectsRequest.setKeys(keys);
            cosClient.deleteObjects(deleteObjectsRequest);
        } catch (CosServiceException e) {
            log.error("从腾讯云COS批量删除文件失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_DELETE_BATCH_FAILED, OssConstants.ERROR_MESSAGE_DELETE_BATCH_FAILED, e);
        }
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名
     * @return 文件字节数组
     */
    @Override
    public byte[] download(String fileName) {
        return download(ossProperties.getBucketName(), fileName);
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
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
            return cosObjectInput.readAllBytes();
        } catch (CosServiceException | IOException e) {
            log.error("从腾讯云COS下载文件失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_DOWNLOAD_FAILED, OssConstants.ERROR_MESSAGE_DOWNLOAD_FAILED, e);
        }
    }

    /**
     * 下载文件到本地
     *
     * @param fileName 文件名
     * @param localPath 本地路径
     */
    @Override
    public void downloadToLocal(String fileName, String localPath) {
        downloadToLocal(ossProperties.getBucketName(), fileName, localPath);
    }

    /**
     * 下载文件到本地
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @param localPath  本地路径
     */
    @Override
    public void downloadToLocal(String bucketName, String fileName, String localPath) {
        try {
            File localFile = new File(localPath);
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
            cosClient.getObject(getObjectRequest, localFile);
        } catch (CosServiceException e) {
            log.error("从腾讯云COS下载文件到本地失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_DOWNLOAD_FAILED, OssConstants.ERROR_MESSAGE_DOWNLOAD_FAILED, e);
        }
    }

    /**
     * 下载文件到输出流
     *
     * @param fileName     文件名
     * @param outputStream 输出流
     */
    @Override
    public void downloadToStream(String fileName, OutputStream outputStream) {
        downloadToStream(ossProperties.getBucketName(), fileName, outputStream);
    }

    /**
     * 下载文件到输出流
     *
     * @param bucketName   存储桶名称
     * @param fileName     文件名
     * @param outputStream 输出流
     */
    @Override
    public void downloadToStream(String bucketName, String fileName, OutputStream outputStream) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
            COSObject cosObject = cosClient.getObject(getObjectRequest);
            COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = cosObjectInput.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            cosObjectInput.close();
        } catch (CosServiceException | IOException e) {
            log.error("从腾讯云COS下载文件到输出流失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_DOWNLOAD_FAILED, OssConstants.ERROR_MESSAGE_DOWNLOAD_FAILED, e);
        }
    }

    /**
     * 获取文件信息
     *
     * @param fileName 文件名
     * @return 文件信息
     */
    @Override
    public OssFile getFileInfo(String fileName) {
        return getFileInfo(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件信息
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件信息
     */
    @Override
    public OssFile getFileInfo(String bucketName, String fileName) {
        try {
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(bucketName, fileName);
            String url = getFileUrl(bucketName, fileName);
            String contentType = objectMetadata.getContentType();
            long size = objectMetadata.getContentLength();
            Date lastModified = objectMetadata.getLastModified();
            // 创建OSS文件对象
            OssFile ossFile = new OssFile();
            ossFile.setName(fileName);
            ossFile.setOriginalName(StringUtils.substringAfterLast(fileName, "/"));
            ossFile.setUrl(url);
            ossFile.setSize(size);
            ossFile.setContentType(contentType);
            ossFile.setBucketName(bucketName);
            ossFile.setPath(fileName);
            ossFile.setCreateTime(lastModified);
            ossFile.setUpdateTime(lastModified);
            ossFile.setStorageType(OssConstants.STORAGE_TYPE_TENCENT);
            return ossFile;
        } catch (CosServiceException e) {
            log.error("获取腾讯云COS文件信息失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_GET_FILE_INFO_FAILED, OssConstants.ERROR_MESSAGE_GET_FILE_INFO_FAILED, e);
        }
    }

    /**
     * 获取文件列表
     *
     * @return 文件列表
     */
    @Override
    public List<OssFile> listFiles() {
        return listFiles(ossProperties.getBucketName());
    }

    /**
     * 获取文件列表
     *
     * @param bucketName 存储桶名称
     * @return 文件列表
     */
    @Override
    public List<OssFile> listFiles(String bucketName) {
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            List<OssFile> ossFiles = new ArrayList<>();
            for (COSObjectSummary cosObjectSummary : objectListing.getObjectSummaries()) {
                String key = cosObjectSummary.getKey();
                // 跳过目录对象
                if (key.endsWith("/")) {
                    continue;
                }
                String url = getFileUrl(bucketName, key);
                String contentType = getContentType(key);
                long size = cosObjectSummary.getSize();
                Date lastModified = cosObjectSummary.getLastModified();
                // 创建OSS文件对象
                OssFile ossFile = new OssFile();
                ossFile.setName(key);
                ossFile.setOriginalName(StringUtils.substringAfterLast(key, "/"));
                ossFile.setUrl(url);
                ossFile.setSize(size);
                ossFile.setContentType(contentType);
                ossFile.setBucketName(bucketName);
                ossFile.setPath(key);
                ossFile.setCreateTime(lastModified);
                ossFile.setUpdateTime(lastModified);
                ossFile.setStorageType(OssConstants.STORAGE_TYPE_TENCENT);
                ossFiles.add(ossFile);
            }
            return ossFiles;
        } catch (CosServiceException e) {
            log.error("获取腾讯云COS文件列表失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_LIST_FILES_FAILED, OssConstants.ERROR_MESSAGE_LIST_FILES_FAILED, e);
        }
    }

    /**
     * 获取文件URL
     *
     * @param fileName 文件名
     * @return 文件URL
     */
    @Override
    public String getFileUrl(String fileName) {
        return getFileUrl(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件URL
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件URL
     */
    @Override
    public String getFileUrl(String bucketName, String fileName) {
        try {
            // 设置URL过期时间
            Date expirationDate = new Date(System.currentTimeMillis() + ossProperties.getExpireTime() * 1000);
            // 生成预签名URL
            URL url = cosClient.generatePresignedUrl(bucketName, fileName, expirationDate);
            return url.toString();
        } catch (CosServiceException e) {
            log.error("获取腾讯云COS文件URL失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_GET_FILE_URL_FAILED, OssConstants.ERROR_MESSAGE_GET_FILE_URL_FAILED, e);
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 文件名
     * @return 是否存在
     */
    @Override
    public boolean exists(String fileName) {
        return exists(ossProperties.getBucketName(), fileName);
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
            cosClient.getObjectMetadata(bucketName, fileName);
            return true;
        } catch (CosServiceException e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
            log.error("检查腾讯云COS文件是否存在失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_CHECK_FILE_FAILED, OssConstants.ERROR_MESSAGE_CHECK_FILE_FAILED, e);
        }
    }

    /**
     * 获取文件大小
     *
     * @param fileName 文件名
     * @return 文件大小
     */
    @Override
    public long getFileSize(String fileName) {
        return getFileSize(ossProperties.getBucketName(), fileName);
    }

    /**
     * 获取文件大小
     *
     * @param bucketName 存储桶名称
     * @param fileName   文件名
     * @return 文件大小
     */
    @Override
    public long getFileSize(String bucketName, String fileName) {
        try {
            ObjectMetadata objectMetadata = cosClient.getObjectMetadata(bucketName, fileName);
            return objectMetadata.getContentLength();
        } catch (CosServiceException e) {
            log.error("获取腾讯云COS文件大小失败: {}", e.getMessage());
            throw new OssException(OssConstants.ERROR_CODE_GET_FILE_SIZE_FAILED, OssConstants.ERROR_MESSAGE_GET_FILE_SIZE_FAILED, e);
        }
    }

    /**
     * 获取存储类型
     *
     * @return 存储类型
     */
    @Override
    public String getStorageType() {
        return OssConstants.STORAGE_TYPE_TENCENT;
    }
}