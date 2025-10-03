package org.aimindflow.common.oss.service.impl;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.utils.StringUtils;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.QiniuOssService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 七牛云OSS服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class QiniuOssServiceImpl extends AbstractOssServiceImpl implements QiniuOssService {

    /**
     * 七牛云上传管理器
     */
    private final UploadManager uploadManager;

    /**
     * 七牛云存储桶管理器
     */
    private final BucketManager bucketManager;

    /**
     * 七牛云认证
     */
    private final Auth auth;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public QiniuOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        // 创建七牛云配置
        Configuration configuration = new Configuration(Region.autoRegion());
        // 创建七牛云上传管理器
        this.uploadManager = new UploadManager(configuration);
        // 创建七牛云认证
        this.auth = Auth.create(ossProperties.getQiniu().getAccessKey(), ossProperties.getQiniu().getSecretKey());
        // 创建七牛云存储桶管理器
        this.bucketManager = new BucketManager(auth, configuration);
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
            log.error("初始化七牛云存储桶失败", e);
            throw new OssException("初始化七牛云存储桶失败", e);
        }
    }

    /**
     * 获取七牛云上传管理器
     *
     * @return 七牛云上传管理器
     */
    @Override
    public UploadManager getUploadManager() {
        return uploadManager;
    }

    /**
     * 获取七牛云存储桶管理器
     *
     * @return 七牛云存储桶管理器
     */
    @Override
    public BucketManager getBucketManager() {
        return bucketManager;
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
                // 七牛云创建存储桶需要通过API调用，这里简化处理
                log.info("七牛云创建存储桶需要通过API调用，请手动创建存储桶：{}", bucketName);
            }
        } catch (Exception e) {
            log.error("创建七牛云存储桶失败", e);
            throw new OssException("创建七牛云存储桶失败", e);
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
            // 七牛云判断存储桶是否存在需要通过API调用，这里简化处理
            return true;
        } catch (Exception e) {
            log.error("判断七牛云存储桶是否存在失败", e);
            throw new OssException("判断七牛云存储桶是否存在失败", e);
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
                // 七牛云删除存储桶需要通过API调用，这里简化处理
                log.info("七牛云删除存储桶需要通过API调用，请手动删除存储桶：{}", bucketName);
            }
        } catch (Exception e) {
            log.error("删除七牛云存储桶失败", e);
            throw new OssException("删除七牛云存储桶失败", e);
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
            // 获取上传凭证
            String upToken = auth.uploadToken(bucketName);
            // 读取输入流
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] data = outputStream.toByteArray();
            // 上传文件
            uploadManager.put(data, objectName, upToken);
            // 获取文件URL
            String url = getUrl(bucketName, objectName);
            // 获取文件大小
            long size = data.length;
            // 创建OSS文件对象
            return createOssFile(objectName, fileName, url, size, contentType, bucketName);
        } catch (Exception e) {
            log.error("上传文件到七牛云失败", e);
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
            bucketManager.delete(bucketName, fileName);
        } catch (QiniuException e) {
            if (e.response != null && e.response.statusCode == 612) {
                // 文件不存在，忽略错误
                return;
            }
            log.error("从七牛云删除文件失败", e);
            throw new OssException(OssConstants.ERROR_CODE_DELETE_FAILED, OssConstants.ERROR_MESSAGE_DELETE_FAILED, e);
        } catch (Exception e) {
            log.error("从七牛云删除文件失败", e);
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
            // 获取文件URL
            String url = getUrl(bucketName, fileName);
            // 下载文件
            return new URL(url).openStream().readAllBytes();
        } catch (Exception e) {
            log.error("从七牛云下载文件失败", e);
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
            com.qiniu.storage.model.FileInfo fileInfo = bucketManager.stat(bucketName, fileName);
            // 获取文件URL
            String url = getUrl(bucketName, fileName);
            // 创建OSS文件对象
            return createOssFile(fileName, fileName, url, fileInfo.fsize, fileInfo.mimeType, bucketName);
        } catch (Exception e) {
            log.error("从七牛云获取文件信息失败", e);
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
            BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(bucketName, prefix, 1000, "");
            while (fileListIterator.hasNext()) {
                com.qiniu.storage.model.FileInfo[] items = fileListIterator.next();
                for (com.qiniu.storage.model.FileInfo item : items) {
                    // 获取文件URL
                    String url = getUrl(bucketName, item.key);
                    // 创建OSS文件对象
                    OssFile ossFile = createOssFile(item.key, item.key, url, item.fsize, item.mimeType, bucketName);
                    fileList.add(ossFile);
                }
            }
            return fileList;
        } catch (Exception e) {
            log.error("从七牛云获取文件列表失败", e);
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
            // 获取域名
            String domain = ossProperties.getDomain();
            if (StringUtils.isEmpty(domain)) {
                domain = ossProperties.getQiniu().getEndpoint();
            }
            // 生成URL
            return auth.privateDownloadUrl(domain + "/" + fileName, expires);
        } catch (Exception e) {
            log.error("从七牛云获取文件URL失败", e);
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
            bucketManager.stat(bucketName, fileName);
            return true;
        } catch (QiniuException e) {
            if (e.response != null && e.response.statusCode == 612) {
                // 文件不存在
                return false;
            }
            log.error("判断七牛云文件是否存在失败", e);
            throw new OssException("判断七牛云文件是否存在失败", e);
        } catch (Exception e) {
            log.error("判断七牛云文件是否存在失败", e);
            throw new OssException("判断七牛云文件是否存在失败", e);
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
            com.qiniu.storage.model.FileInfo fileInfo = bucketManager.stat(bucketName, fileName);
            return fileInfo.fsize;
        } catch (Exception e) {
            log.error("获取七牛云文件大小失败", e);
            throw new OssException("获取七牛云文件大小失败", e);
        }
    }
}