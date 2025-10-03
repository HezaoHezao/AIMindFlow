package org.aimindflow.common.encrypt.factory;

import org.aimindflow.common.encrypt.service.EncryptService;
import org.aimindflow.common.encrypt.service.impl.AesEncryptServiceImpl;
import org.aimindflow.common.encrypt.service.impl.RsaEncryptServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 加密服务工厂类
 *
 * @author HezaoHezao
 */
@Component
public class EncryptServiceFactory {

    private final AesEncryptServiceImpl aesEncryptService;
    private final RsaEncryptServiceImpl rsaEncryptService;

    @Autowired
    public EncryptServiceFactory(AesEncryptServiceImpl aesEncryptService, 
                                RsaEncryptServiceImpl rsaEncryptService) {
        this.aesEncryptService = aesEncryptService;
        this.rsaEncryptService = rsaEncryptService;
    }

    /**
     * 获取加密服务
     *
     * @param encryptType 加密类型（AES、RSA）
     * @return 加密服务实例
     */
    public EncryptService getEncryptService(String encryptType) {
        if (encryptType == null || encryptType.isEmpty()) {
            throw new IllegalArgumentException("加密类型不能为空");
        }

        encryptType = encryptType.toUpperCase();
        switch (encryptType) {
            case "AES":
                return aesEncryptService;
            case "RSA":
                return rsaEncryptService;
            default:
                throw new IllegalArgumentException("不支持的加密类型: " + encryptType);
        }
    }
}