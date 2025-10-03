package org.aimindflow.common.encrypt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.encrypt.service.EncryptService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES加密服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class AesEncryptServiceImpl implements EncryptService {

    private static final String AES_ALGORITHM = "AES";
    private static final String SHA_256_ALGORITHM = "SHA-256";
    private static final int DEFAULT_KEY_SIZE = 256;

    @Override
    public String encrypt(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyBytes(key), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("AES加密失败", e);
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public String decrypt(String encryptedData, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getKeyBytes(key), AES_ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("AES解密失败", e);
            throw new RuntimeException("解密失败", e);
        }
    }

    @Override
    public String hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("哈希计算失败", e);
            throw new RuntimeException("哈希计算失败", e);
        }
    }

    @Override
    public String hash(String data, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            digest.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("带盐值的哈希计算失败", e);
            throw new RuntimeException("带盐值的哈希计算失败", e);
        }
    }

    @Override
    public boolean verifyHash(String data, String hashData) {
        String calculatedHash = hash(data);
        return calculatedHash.equals(hashData);
    }

    @Override
    public boolean verifyHash(String data, String salt, String hashData) {
        String calculatedHash = hash(data, salt);
        return calculatedHash.equals(hashData);
    }

    @Override
    public String generateKey(int keySize) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(keySize);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();
            return Base64.getEncoder().encodeToString(keyBytes);
        } catch (NoSuchAlgorithmException e) {
            log.error("密钥生成失败", e);
            throw new RuntimeException("密钥生成失败", e);
        }
    }

    /**
     * 获取密钥字节数组
     *
     * @param key 密钥字符串
     * @return 密钥字节数组
     */
    private byte[] getKeyBytes(String key) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
            return digest.digest(key.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            log.error("密钥处理失败", e);
            throw new RuntimeException("密钥处理失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}