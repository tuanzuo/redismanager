package com.tz.redismanager.test;

import com.tz.redismanager.util.RSAUtil;
import com.tz.redismanager.util.RsaException;
import com.tz.redismanager.util.RsaKey;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-30 21:45
 **/
public class RSAUtilTest {

    public static void main(String[] args) throws RsaException {
        String charset = "UTF-8";
        System.err.println("--------生成公钥和私钥---------------");
        RsaKey rsaKey = RSAUtil.generateKey(RSAUtil.KEYSIZE_1024);
        // RsaKey rsaKey = RsaUtil.generateKey(KEYSIZE_2048);//使用2048的时候会报错
        String publicKey = rsaKey.getPublicKey();
        String privateKey = rsaKey.getPrivateKey();
        System.err.println("公钥：" + publicKey);
        System.err.println("私钥：" + privateKey);
        System.err.println("---------公钥加密-->私钥解密-----------------");
        String content = "123abc";
//		String content = UUID.randomUUID().toString().replace("-", "");
        System.err.println("待加密内容:" + content);
        String encodeStr = RSAUtil.rsaPublicEncrypt(content, publicKey, charset);
        System.err.println("公钥加密后：" + encodeStr);
        System.err.println("私钥解密后：" + RSAUtil.rsaPrivateDecrypt(encodeStr, privateKey, charset));
        System.err.println("---------私钥加密-->公钥解密-----------------");
        String contentTwo = "123abc";
//		String contentTwo = UUID.randomUUID().toString().replace("-", "");
        System.err.println("待加密内容:" + contentTwo);
        String encodeStrTwo = RSAUtil.rsaPrivateEncrypt(contentTwo, privateKey, charset);
        System.err.println("私钥加密后：" + encodeStrTwo);
        System.err.println("公钥解密后：" + RSAUtil.rsaPublicDecrypt(encodeStrTwo, publicKey, charset));
    }
}
