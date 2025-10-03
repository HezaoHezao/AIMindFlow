package org.aimindflow.common.encrypt.service;

/**
 * 加密服务接口
 *
 * @author HezaoHezao
 */
public interface EncryptService {

    /**
     * 对数据进行加密
     *
     * @param data 待加密的数据
     * @param key  加密密钥
     * @return 加密后的数据
     */
    String encrypt(String data, String key);

    /**
     * 对数据进行解密
     *
     * @param encryptedData 已加密的数据
     * @param key           解密密钥
     * @return 解密后的数据
     */
    String decrypt(String encryptedData, String key);

    /**
     * 生成数据的哈希值
     *
     * @param data 待哈希的数据
     * @return 哈希值
     */
    String hash(String data);

    /**
     * 生成数据的哈希值（带盐值）
     *
     * @param data 待哈希的数据
     * @param salt 盐值
     * @return 哈希值
     */
    String hash(String data, String salt);

    /**
     * 验证哈希值
     *
     * @param data     原始数据
     * @param hashData 哈希值
     * @return 是否匹配
     */
    boolean verifyHash(String data, String hashData);

    /**
     * 验证哈希值（带盐值）
     *
     * @param data     原始数据
     * @param salt     盐值
     * @param hashData 哈希值
     * @return 是否匹配
     */
    boolean verifyHash(String data, String salt, String hashData);

    /**
     * 生成随机密钥
     *
     * @param keySize 密钥长度（位）
     * @return 随机密钥
     */
    String generateKey(int keySize);
}