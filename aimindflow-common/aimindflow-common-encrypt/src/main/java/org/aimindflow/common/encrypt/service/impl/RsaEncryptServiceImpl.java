package org.aimindflow.common.encrypt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.encrypt.service.EncryptService;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加密服务实现
 *
 * @author HezaoHezao
 */
@Slf4j
@Service
public class RsaEncryptServiceImpl implements EncryptService {

    private static final String RSA_ALGORITHM = "RSA";
    private static final String SHA_256_ALGORITHM = "SHA-256";
    private static final int DEFAULT_KEY_SIZE = 2048;

    @Override
    public String encrypt(String data, String key) {
        try {
            // 将Base64编码的公钥转换为PublicKey对象
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 使用公钥加密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            log.error("RSA加密失败", e);
            throw new RuntimeException("加密失败", e);
        }
    }

    @Override
    public String decrypt(String encryptedData, String key) {
        try {
            // 将Base64编码的私钥转换为PrivateKey对象
            byte[] keyBytes = Base64.getDecoder().decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

            // 使用私钥解密
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA解密失败", e);
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
            // 生成RSA密钥对
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // 获取公钥和私钥
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 将公钥和私钥编码为Base64字符串
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            // 返回公钥和私钥的组合（实际使用时应该分开返回）
            return "Public Key: " + publicKeyString + "\nPrivate Key: " + privateKeyString;
        } catch (NoSuchAlgorithmException e) {
            log.error("RSA密钥对生成失败", e);
            throw new RuntimeException("RSA密钥对生成失败", e);
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