package com.tz.redismanager.test;

import com.tz.redismanager.util.RSAUtils;
import com.tz.redismanager.util.RsaException;
import com.tz.redismanager.util.RsaKey;

import java.util.UUID;

/**
 * <p></p>
 *
 * @version 1.0
 * @time 2019-06-30 21:45
 **/
public class RSAUtilTest {

    public static void main(String[] args) throws RsaException {
        String charset = "UTF-8";
        System.err.println("--------生成公钥和私钥---------------");
        RsaKey rsaKey = RSAUtils.generatorKey(RSAUtils.KEYSIZE_1024);
//        RsaKey rsaKey = RSAUtils.generatorKey(RSAUtils.KEYSIZE_2048);
        String publicKey = rsaKey.getPublicKey();
        String privateKey = rsaKey.getPrivateKey();
        System.err.println("公钥：" + publicKey);
        System.err.println("私钥：" + privateKey);
        System.err.println("---------公钥加密-->私钥解密-----------------");
		String content = UUID.randomUUID().toString();
        System.err.println("待加密内容:" + content);
        String encodeStr = RSAUtils.rsaPublicEncrypt(content, publicKey, charset);
        System.err.println("公钥加密后：" + encodeStr);
        System.err.println("私钥解密后：" + RSAUtils.rsaPrivateDecrypt(encodeStr, privateKey, charset));
        System.err.println("---------私钥加密-->公钥解密-----------------");
		String contentTwo = UUID.randomUUID().toString();
        System.err.println("待加密内容：" + contentTwo);
        String encodeStrTwo = RSAUtils.rsaPrivateEncrypt(contentTwo, privateKey, charset);
        System.err.println("私钥加密后：" + encodeStrTwo);
        System.err.println("公钥解密后：" + RSAUtils.rsaPublicDecrypt(encodeStrTwo, publicKey, charset));
    }
}
