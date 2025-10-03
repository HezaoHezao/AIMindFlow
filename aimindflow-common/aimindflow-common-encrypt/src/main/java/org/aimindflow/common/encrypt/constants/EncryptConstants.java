package org.aimindflow.common.encrypt.constants;

/**
 * 加密常量类
 *
 * @author HezaoHezao
 */
public class EncryptConstants {

    /**
     * 加密类型常量
     */
    public static class EncryptType {
        /** AES加密 */
        public static final String AES = "AES";
        /** RSA加密 */
        public static final String RSA = "RSA";
    }

    /**
     * 密钥长度常量
     */
    public static class KeySize {
        /** AES-128密钥长度 */
        public static final int AES_128 = 128;
        /** AES-192密钥长度 */
        public static final int AES_192 = 192;
        /** AES-256密钥长度 */
        public static final int AES_256 = 256;
        /** RSA-1024密钥长度 */
        public static final int RSA_1024 = 1024;
        /** RSA-2048密钥长度 */
        public static final int RSA_2048 = 2048;
        /** RSA-4096密钥长度 */
        public static final int RSA_4096 = 4096;
    }

    /**
     * 哈希算法常量
     */
    public static class HashAlgorithm {
        /** MD5哈希算法 */
        public static final String MD5 = "MD5";
        /** SHA-1哈希算法 */
        public static final String SHA_1 = "SHA-1";
        /** SHA-256哈希算法 */
        public static final String SHA_256 = "SHA-256";
        /** SHA-512哈希算法 */
        public static final String SHA_512 = "SHA-512";
    }

    /**
     * 错误消息常量
     */
    public static class ErrorMessage {
        /** 加密失败 */
        public static final String ENCRYPT_FAILED = "加密失败: %s";
        /** 解密失败 */
        public static final String DECRYPT_FAILED = "解密失败: %s";
        /** 哈希计算失败 */
        public static final String HASH_FAILED = "哈希计算失败: %s";
        /** 密钥生成失败 */
        public static final String KEY_GENERATION_FAILED = "密钥生成失败: %s";
        /** 不支持的加密类型 */
        public static final String UNSUPPORTED_ENCRYPT_TYPE = "不支持的加密类型: %s";
    }
}