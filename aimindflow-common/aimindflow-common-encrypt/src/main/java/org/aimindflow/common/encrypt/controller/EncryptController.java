package org.aimindflow.common.encrypt.controller;

import lombok.extern.slf4j.Slf4j;
import org.aimindflow.common.core.domain.R;
import org.aimindflow.common.encrypt.factory.EncryptServiceFactory;
import org.aimindflow.common.encrypt.service.EncryptService;
import org.aimindflow.common.encrypt.utils.EncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 加密控制器
 *
 * @author HezaoHezao
 */
@Slf4j
@RestController
@RequestMapping("/encrypt")
public class EncryptController {

    private final EncryptServiceFactory encryptServiceFactory;

    @Autowired
    public EncryptController(EncryptServiceFactory encryptServiceFactory) {
        this.encryptServiceFactory = encryptServiceFactory;
    }

    /**
     * 加密数据
     *
     * @param data        待加密数据
     * @param key         加密密钥
     * @param encryptType 加密类型（AES、RSA）
     * @return 加密结果
     */
    @PostMapping("/encrypt")
    public R<String> encrypt(@RequestParam("data") String data,
                           @RequestParam("key") String key,
                           @RequestParam("type") String encryptType) {
        try {
            EncryptService encryptService = encryptServiceFactory.getEncryptService(encryptType);
            String encryptedData = encryptService.encrypt(data, key);
            return R.ok(encryptedData);
        } catch (Exception e) {
            log.error("加密失败", e);
            return R.fail("加密失败: " + e.getMessage());
        }
    }

    /**
     * 解密数据
     *
     * @param encryptedData 已加密数据
     * @param key           解密密钥
     * @param encryptType   加密类型（AES、RSA）
     * @return 解密结果
     */
    @PostMapping("/decrypt")
    public R<String> decrypt(@RequestParam("data") String encryptedData,
                           @RequestParam("key") String key,
                           @RequestParam("type") String encryptType) {
        try {
            EncryptService encryptService = encryptServiceFactory.getEncryptService(encryptType);
            String decryptedData = encryptService.decrypt(encryptedData, key);
            return R.ok(decryptedData);
        } catch (Exception e) {
            log.error("解密失败", e);
            return R.fail("解密失败: " + e.getMessage());
        }
    }

    /**
     * 计算哈希值
     *
     * @param data 待哈希数据
     * @param salt 盐值（可选）
     * @return 哈希结果
     */
    @PostMapping("/hash")
    public R<String> hash(@RequestParam("data") String data,
                         @RequestParam(value = "salt", required = false) String salt) {
        try {
            String hashData;
            if (salt != null && !salt.isEmpty()) {
                hashData = EncryptUtils.hash(data, salt);
            } else {
                hashData = EncryptUtils.hash(data);
            }
            return R.ok(hashData);
        } catch (Exception e) {
            log.error("哈希计算失败", e);
            return R.fail("哈希计算失败: " + e.getMessage());
        }
    }

    /**
     * 生成密钥
     *
     * @param encryptType 加密类型（AES、RSA）
     * @param keySize     密钥长度（位）
     * @return 生成的密钥
     */
    @PostMapping("/generate-key")
    public R<String> generateKey(@RequestParam("type") String encryptType,
                               @RequestParam(value = "keySize", defaultValue = "256") int keySize) {
        try {
            EncryptService encryptService = encryptServiceFactory.getEncryptService(encryptType);
            String key = encryptService.generateKey(keySize);
            return R.ok(key);
        } catch (Exception e) {
            log.error("密钥生成失败", e);
            return R.fail("密钥生成失败: " + e.getMessage());
        }
    }
}