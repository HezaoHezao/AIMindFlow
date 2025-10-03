package org.aimindflow.common.oss.service;

/**
 * 本地存储服务接口
 *
 * @author HezaoHezao
 */
public interface LocalOssService extends OssService {

    /**
     * 创建存储目录
     *
     * @param path 存储路径
     */
    void createDirectory(String path);

    /**
     * 判断存储目录是否存在
     *
     * @param path 存储路径
     * @return 是否存在
     */
    boolean directoryExists(String path);

    /**
     * 删除存储目录
     *
     * @param path 存储路径
     */
    void removeDirectory(String path);
}