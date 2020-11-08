package com.tz.redismanager.test;

import com.tz.redismanager.util.AESUtils;

/**
 * <p>AES加解密测试</p>
 *
 * @version 1.0
 * @time 2020-11-08 16:55
 **/
public class AESUtilTest {

    public static void main(String[] args) throws Exception {
        String content = "hello word 世界那么大";
        System.err.println("原内容-->" + content);
        String key = AESUtils.generatorKey(AESUtils.AES_KEY_256_SIZE);
        System.err.println("AES key-->" + key);
        String encryptContent = AESUtils.encrypt(content, key);
        System.err.println("AES 加密后的内容-->" + encryptContent);
        System.err.println("AES 解密后的内容-->" + AESUtils.decrypt(encryptContent, key));
    }

}
