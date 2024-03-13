package com.myrpc.net;

import com.myrpc.protocol.Enums.EncryptionMethod;
import lombok.Data;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

//TODO 等待实现
@Data
public class AESConvertor implements EncipherConvertor {


    /**
     * 经过base64编码后的密钥
     */
    String encryptionKey;

    public AESConvertor(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    private static byte[] encrypt(byte[] msg, String encryptionKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(encryptionKey);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        // 执行加密
        byte[] encryptedBytes = cipher.doFinal(msg);
        return Base64.getEncoder().encode(encryptedBytes);
    }

    private static byte[] decrypt(byte[] msg, String secretKey) throws Exception {
        // 将Base64编码的密钥转换回字节数组
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // 初始化Cipher对象
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);

        // 执行解密
        return cipher.doFinal(Base64.getDecoder().decode(msg));
    }


    /**
     * @param msg 原始数据
     * @return
     */
    @Override
    public byte[] encrypt(byte[] msg) throws Exception {
        return encrypt(msg,encryptionKey);
    }

    /**
     * @param msg 未解密的数据流
     * @return
     */
    @Override
    public byte[] decrypt(byte[] msg) throws Exception {
        return decrypt(msg,encryptionKey);
    }
}
