package com.tz.redismanager.token.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>AES加密</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-11-07 22:24
 **/
public class AESUtils {

    //参数分别代表 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    private static final String AES = "AES";

    /**
     * AES密钥的长度：128/192/256(bits)位，即：16/24/32bytes；
     * 注意：256位密钥需要获得无政策限制权限文件才能正常使用，参考/redismanager/doc/jce/readme.txt文件
     */
    public static final int AES_KEY_256_SIZE = 256;
    public static final int AES_KEY_128_SIZE = 128;

    /**
     * 生成加密的KEY
     *
     * @return
     */
    public static String generatorKey(int keySize) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(keySize);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        return Base64.encodeBase64String(keyBytes);
    }

    /**
     * 加密
     *
     * @param content    加密的字符串
     * @param encryptKey key值
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String encryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(encryptKey), AES));
        byte[] b = cipher.doFinal(content.getBytes());
        // 采用base64算法进行转码,避免出现中文乱码
        return Base64.encodeBase64String(b);

    }

    /**
     * 解密
     *
     * @param encryptContent 解密的字符串
     * @param decryptKey     解密的key值
     * @return
     * @throws Exception
     */
    public static String decrypt(String encryptContent, String decryptKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(decryptKey), AES));
        // 采用base64算法进行转码,避免出现中文乱码
        byte[] encryptBytes = Base64.decodeBase64(encryptContent);
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

}

