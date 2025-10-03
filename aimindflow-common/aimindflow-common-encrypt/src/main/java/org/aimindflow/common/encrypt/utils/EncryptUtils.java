package org.aimindflow.common.encrypt.utils;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.encrypt.factory.EncryptServiceFactory;
import org.aimindflow.common.encrypt.service.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 加密工具类
 *
 * @author HezaoHezao
 */
@Slf4j
@Component
public class EncryptUtils {

    private static EncryptServiceFactory encryptServiceFactory;

    @Autowired
    public void setEncryptServiceFactory(EncryptServiceFactory encryptServiceFactory) {
        EncryptUtils.encryptServiceFactory = encryptServiceFactory;
    }

    /**
     * AES加密
     *
     * @param data 待加密数据
     * @param key  加密密钥
     * @return 加密后的数据
     */
    public static String aesEncrypt(String data, String key) {
        return getEncryptService("AES").encrypt(data, key);
    }

    /**
     * AES解密
     *
     * @param encryptedData 已加密数据
     * @param key           解密密钥
     * @return 解密后的数据
     */
    public static String aesDecrypt(String encryptedData, String key) {
        return getEncryptService("AES").decrypt(encryptedData, key);
    }

    /**
     * RSA加密（使用公钥）
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后的数据
     */
    public static String rsaEncrypt(String data, String publicKey) {
        return getEncryptService("RSA").encrypt(data, publicKey);
    }

    /**
     * RSA解密（使用私钥）
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥
     * @return 解密后的数据
     */
    public static String rsaDecrypt(String encryptedData, String privateKey) {
        return getEncryptService("RSA").decrypt(encryptedData, privateKey);
    }

    /**
     * 计算哈希值
     *
     * @param data 待哈希数据
     * @return 哈希值
     */
    public static String hash(String data) {
        return getEncryptService("AES").hash(data);
    }

    /**
     * 计算带盐值的哈希值
     *
     * @param data 待哈希数据
     * @param salt 盐值
     * @return 哈希值
     */
    public static String hash(String data, String salt) {
        return getEncryptService("AES").hash(data, salt);
    }

    /**
     * 验证哈希值
     *
     * @param data     原始数据
     * @param hashData 哈希值
     * @return 是否匹配
     */
    public static boolean verifyHash(String data, String hashData) {
        return getEncryptService("AES").verifyHash(data, hashData);
    }

    /**
     * 验证带盐值的哈希值
     *
     * @param data     原始数据
     * @param salt     盐值
     * @param hashData 哈希值
     * @return 是否匹配
     */
    public static boolean verifyHash(String data, String salt, String hashData) {
        return getEncryptService("AES").verifyHash(data, salt, hashData);
    }

    /**
     * 生成AES密钥
     *
     * @param keySize 密钥长度（位）
     * @return AES密钥
     */
    public static String generateAesKey(int keySize) {
        return getEncryptService("AES").generateKey(keySize);
    }

    /**
     * 生成RSA密钥对
     *
     * @param keySize 密钥长度（位）
     * @return RSA密钥对（公钥和私钥）
     */
    public static String generateRsaKeyPair(int keySize) {
        return getEncryptService("RSA").generateKey(keySize);
    }

    /**
     * 获取加密服务
     *
     * @param encryptType 加密类型
     * @return 加密服务实例
     */
    private static EncryptService getEncryptService(String encryptType) {
        if (encryptServiceFactory == null) {
            throw new IllegalStateException("加密服务工厂未初始化");
        }
        return encryptServiceFactory.getEncryptService(encryptType);
    }
}