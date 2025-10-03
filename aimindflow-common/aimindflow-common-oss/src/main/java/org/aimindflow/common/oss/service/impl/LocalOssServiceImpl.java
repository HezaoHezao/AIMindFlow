package org.aimindflow.common.oss.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.utils.StringUtils;
import org.aimindflow.common.core.utils.file.FileUtils;
import org.aimindflow.common.oss.config.OssProperties;
import org.aimindflow.common.oss.constant.OssConstants;
import org.aimindflow.common.oss.exception.OssException;
import org.aimindflow.common.oss.model.OssFile;
import org.aimindflow.common.oss.service.LocalOssService;
import org.springframework.web.util.UriUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 本地存储服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
public class LocalOssServiceImpl extends AbstractOssServiceImpl implements LocalOssService {

    /**
     * 存储路径
     */
    private final String storagePath;

    /**
     * 访问前缀
     */
    private final String accessPrefix;

    /**
     * 构造方法
     *
     * @param ossProperties OSS配置属性
     */
    public LocalOssServiceImpl(OssProperties ossProperties) {
        super(ossProperties);
        this.storagePath = ossProperties.getLocal().getStoragePath();
        this.accessPrefix = ossProperties.getLocal().getAccessPrefix();
        // 初始化存储目录
        initDirectory();
    }

    /**
     * 初始化存储目录
     */
    private void initDirectory() {
        try {
            // 判断存储目录是否存在
            if (!directoryExists(storagePath)) {
                // 创建存储目录
                createDirectory(storagePath);
            }
        } catch (Exception e) {
            log.error("初始化本地存储目录失败", e);
            throw new OssException("初始化本地存储目录失败", e);
        }
    }

    /**
     * 创建存储目录
     *
     * @param path 存储路径
     */
    @Override
    public void createDirectory(String path) {
        try {
            Path directory = Paths.get(path);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (Exception e) {
            log.error("创建本地存储目录失败", e);
            throw new OssException("创建本地存储目录失败", e);
        }
    }

    /**
     * 判断存储目录是否存在
     *
     * @param path 存储路径
     * @return 是否存在
     */
    @Override
    public boolean directoryExists(String path) {
        Path directory = Paths.get(path);
        return Files.exists(directory) && Files.isDirectory(directory);
    }

    /**
     * 删除存储目录
     *
     * @param path 存储路径
     */
    @Override
    public void removeDirectory(String path) {
        try {
            Path directory = Paths.get(path);
            if (Files.exists(directory) && Files.isDirectory(directory)) {
                FileUtils.deleteDirectory(directory.toFile());
            }
        } catch (Exception e) {
            log.error("删除本地存储目录失败", e);
            throw new OssException("删除本地存储目录失败", e);
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
            // 创建目录
            String filePath = storagePath + File.separator + bucketName + File.separator + objectName;
            File file = new File(filePath);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            // 写入文件
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            // 获取文件URL
            String url = getUrl(bucketName, objectName);
            // 获取文件大小
            long size = getSize(bucketName, objectName);
            // 创建OSS文件对象
            return createOssFile(objectName, fileName, url, size, contentType, bucketName);
        } catch (Exception e) {
            log.error("上传文件到本地存储失败", e);
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
            String filePath = storagePath + File.separator + bucketName + File.separator + fileName;
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("从本地存储删除文件失败", e);
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
            String filePath = storagePath + File.separator + bucketName + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                throw new OssException(OssConstants.ERROR_CODE_FILE_NOT_FOUND, OssConstants.ERROR_MESSAGE_FILE_NOT_FOUND);
            }
            return Files.readAllBytes(file.toPath());
        } catch (Exception e) {
            log.error("从本地存储下载文件失败", e);
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
            String filePath = storagePath + File.separator + bucketName + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                throw new OssException(OssConstants.ERROR_CODE_FILE_NOT_FOUND, OssConstants.ERROR_MESSAGE_FILE_NOT_FOUND);
            }
            // 获取文件URL
            String url = getUrl(bucketName, fileName);
            // 获取内容类型
            String contentType = getContentType(fileName);
            // 创建OSS文件对象
            return createOssFile(fileName, fileName, url, file.length(), contentType, bucketName);
        } catch (Exception e) {
            log.error("从本地存储获取文件信息失败", e);
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
            String dirPath = storagePath + File.separator + bucketName;
            if (StringUtils.isNotEmpty(prefix)) {
                dirPath += File.separator + prefix;
            }
            Path directory = Paths.get(dirPath);
            if (!Files.exists(directory) || !Files.isDirectory(directory)) {
                return fileList;
            }
            // 遍历文件
            try (Stream<Path> paths = Files.walk(directory)) {
                List<Path> pathList = paths.filter(Files::isRegularFile).collect(Collectors.toList());
                for (Path path : pathList) {
                    String relativePath = directory.relativize(path).toString().replace("\\", "/");
                    String fileName = prefix + (StringUtils.isNotEmpty(prefix) && !prefix.endsWith("/") ? "/" : "") + relativePath;
                    // 获取文件URL
                    String url = getUrl(bucketName, fileName);
                    // 获取内容类型
                    String contentType = getContentType(fileName);
                    // 创建OSS文件对象
                    OssFile ossFile = createOssFile(fileName, fileName, url, Files.size(path), contentType, bucketName);
                    fileList.add(ossFile);
                }
            }
            return fileList;
        } catch (Exception e) {
            log.error("从本地存储获取文件列表失败", e);
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
            // 编码文件名
            String encodedFileName = UriUtils.encode(fileName, StandardCharsets.UTF_8);
            // 构建URL
            String domain = ossProperties.getDomain();
            if (StringUtils.isEmpty(domain)) {
                domain = "http://localhost:8080";
            }
            return domain + accessPrefix + "/" + bucketName + "/" + encodedFileName;
        } catch (Exception e) {
            log.error("从本地存储获取文件URL失败", e);
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
        String filePath = storagePath + File.separator + bucketName + File.separator + fileName;
        File file = new File(filePath);
        return file.exists() && file.isFile();
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
            String filePath = storagePath + File.separator + bucketName + File.separator + fileName;
            File file = new File(filePath);
            if (!file.exists()) {
                throw new OssException(OssConstants.ERROR_CODE_FILE_NOT_FOUND, OssConstants.ERROR_MESSAGE_FILE_NOT_FOUND);
            }
            return file.length();
        } catch (Exception e) {
            log.error("获取本地存储文件大小失败", e);
            throw new OssException("获取本地存储文件大小失败", e);
        }
    }
}