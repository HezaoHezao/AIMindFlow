package org.aimindflow.common.oss.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * OSS文件信息
 *
 * @author HezaoHezao
 */
@Data
@Accessors(chain = true)
public class OssFile implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 原始文件名
     */
    private String originalFileName;

    /**
     * 文件URL
     */
    private String url;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 存储路径
     */
    private String path;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后修改时间
     */
    private String lastModified;

    /**
     * 存储类型
     */
    private String storageType;

    /**
     * 扩展信息
     */
    private Object metadata;
}