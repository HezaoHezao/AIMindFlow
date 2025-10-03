package org.aimindflow.common.oss.service.impl;

import com.obs.services.ObsClient;
import com.obs.services.exception.ObsException;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.utils.StringUtils;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.HuaweiOssService;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 华为云OBS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class HuaweiOssServiceImpl extends AbstractOssServiceImpl implements HuaweiOssService {

    /**
     * 华为云OBS客户端
     */
    private final ObsClient obsClient;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public HuaweiOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        // 初始化华为云OBS客户端
        this.obsClient = new ObsClient(
                ossProperties.getHuawei().getAccessKey(),
                ossProperties.getHuawei().getSecretKey(),
                ossProperties.getHuawei().getEndpoint()
        );
        // 创建默认存储桶
        createBucket(ossProperties.getBucketName());
    }

    /**
     * 获取华为云OBS客户端
     *
     * @return 华为云OBS客户端
     */
    @Override
    public ObsClient getObsClient() {
        return this.obsClient;
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
                CreateBucketRequest request = new CreateBucketRequest();
                request.setBucketName(bucketName);
                // 设置存储桶访问权限为私有
                request.setAcl(AccessControlList.REST_CANNED_PRIVATE);
                // 设置存储桶所在的区域
                request.setLocation(ossProperties.getRegion());
                obsClient.createBucket(request);
                log.info("创建华为云OBS存储桶成功: {}", bucketName);
            }
        } catch (ObsException e) {
            log.error("创建华为云OBS存储桶失败: {}", e.getMessage());
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
            HeadBucketRequest request = new HeadBucketRequest(bucketName);
            boolean exists = obsClient.headBucket(request);
            return exists;
        } catch (ObsException e) {
            if (e.getResponseCode() == 404) {
                return false;
            }
            log.error("检查华为云OBS存储桶是否存在失败: {}", e.getMessage());
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
                obsClient.deleteBucket(bucketName);
                log.info("删除华为云OBS存储桶成功: {}", bucketName);
            }
        } catch (ObsException e) {
            log.error("删除华为云OBS存储桶失败: {}", e.getMessage());
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
            PutObjectRequest request = new PutObjectRequest();
            request.setBucketName(bucketName);
            request.setObjectKey(objectName);
            request.setInput(inputStream);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            request.setMetadata(metadata);
            // 上传文件
            obsClient.putObject(request);
            // 获取文件URL
            String url = getFileUrl(bucketName, objectName);
            // 创建OSS文件对象
            return createOssFile(bucketName, objectName, fileName, url, contentType);
        } catch (ObsException e) {
            log.error("上传文件到华为云OBS失败: {}", e.getMessage());
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
            obsClient.deleteObject(bucketName, fileName);
        } catch (ObsException e) {
            log.error("从华为云OBS删除文件失败: {}", e.getMessage());
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
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
            List<KeyAndVersion> keys = new ArrayList<>();
            for (String fileName : fileNames) {
                keys.add(new KeyAndVersion(fileName));
            }
            request.setKeyAndVersions(keys.toArray(new KeyAndVersion[0]));
            obsClient.deleteObjects(request);
        } catch (ObsException e) {
            log.error("从华为云OBS批量删除文件失败: {}", e.getMessage());
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
            GetObjectRequest request = new GetObjectRequest(bucketName, fileName);
            ObsObject obsObject = obsClient.getObject(request);
            return obsObject.getObjectContent().readAllBytes();
        } catch (ObsException | java.io.IOException e) {
            log.error("从华为云OBS下载文件失败: {}", e.getMessage());
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
            GetObjectRequest request = new GetObjectRequest(bucketName, fileName);
            obsClient.getObject(request, new File(localPath));
        } catch (ObsException e) {
            log.error("从华为云OBS下载文件到本地失败: {}", e.getMessage());
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
            GetObjectRequest request = new GetObjectRequest(bucketName, fileName);
            ObsObject obsObject = obsClient.getObject(request);
            InputStream inputStream = obsObject.getObjectContent();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
        } catch (ObsException | java.io.IOException e) {
            log.error("从华为云OBS下载文件到输出流失败: {}", e.getMessage());
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
            GetObjectMetadataRequest request = new GetObjectMetadataRequest(bucketName, fileName);
            ObjectMetadata objectMetadata = obsClient.getObjectMetadata(request);
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
            ossFile.setStorageType(OssConstants.STORAGE_TYPE_HUAWEI);
            return ossFile;
        } catch (ObsException e) {
            log.error("获取华为云OBS文件信息失败: {}", e.getMessage());
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
            ListObjectsRequest request = new ListObjectsRequest(bucketName);
            ObjectListing result = obsClient.listObjects(request);
            List<OssFile> ossFiles = new ArrayList<>();
            for (ObsObject obsObject : result.getObjects()) {
                String key = obsObject.getObjectKey();
                // 跳过目录对象
                if (key.endsWith("/")) {
                    continue;
                }
                String url = getFileUrl(bucketName, key);
                String contentType = getContentType(key);
                long size = obsObject.getMetadata().getContentLength();
                Date lastModified = obsObject.getMetadata().getLastModified();
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
                ossFile.setStorageType(OssConstants.STORAGE_TYPE_HUAWEI);
                ossFiles.add(ossFile);
            }
            return ossFiles;
        } catch (ObsException e) {
            log.error("获取华为云OBS文件列表失败: {}", e.getMessage());
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
            TemporarySignatureRequest request = new TemporarySignatureRequest(HttpMethodEnum.GET, ossProperties.getExpireTime());
            request.setBucketName(bucketName);
            request.setObjectKey(fileName);
            TemporarySignatureResponse response = obsClient.createTemporarySignature(request);
            return response.getSignedUrl();
        } catch (ObsException e) {
            log.error("获取华为云OBS文件URL失败: {}", e.getMessage());
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
            GetObjectMetadataRequest request = new GetObjectMetadataRequest(bucketName, fileName);
            obsClient.getObjectMetadata(request);
            return true;
        } catch (ObsException e) {
            if (e.getResponseCode() == 404) {
                return false;
            }
            log.error("检查华为云OBS文件是否存在失败: {}", e.getMessage());
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
            GetObjectMetadataRequest request = new GetObjectMetadataRequest(bucketName, fileName);
            ObjectMetadata objectMetadata = obsClient.getObjectMetadata(request);
            return objectMetadata.getContentLength();
        } catch (ObsException e) {
            log.error("获取华为云OBS文件大小失败: {}", e.getMessage());
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
        return OssConstants.STORAGE_TYPE_HUAWEI;
    }
}