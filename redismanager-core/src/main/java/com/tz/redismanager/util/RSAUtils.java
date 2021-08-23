package com.tz.redismanager.util;

import com.tz.redismanager.exception.RmException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加解密工具类
 * RSA非对称加密和解密，当使用keySize=2048生成的秘钥进行解密的时候会报错，
 * 但是使用keySize=1024生成的秘钥进行解密的时候则不会报错
 *
 * @author tuanzuo
 */
public class RSAUtils {

    private static final String RSA_ALGORITHM = "RSA";
    public static final int KEYSIZE_1024 = 1024;
    /**
     * 使用2048生成的公钥和私钥加解密的时候会报错
     */
    public static final int KEYSIZE_2048 = 2048;

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    public static final String CHARSET_UTF8 = "UTF-8";

    /**
     * 生成密钥
     *
     * @param keySize 至少1024，否则不安全
     * @return
     */
    public static RsaKey generatorKey(int keySize) {
        keySize = keySize > 0 ? keySize : KEYSIZE_1024;

        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RmException(e);
        }

        // 初始化
        keyPairGen.initialize(keySize);
        // 生成密钥对
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RsaKey rsaKey = new RsaKey();
        // 公钥
        rsaKey.setPublicKey(Base64.encodeBase64String(((RSAPublicKey) keyPair.getPublic()).getEncoded()));
        // 私钥
        rsaKey.setPrivateKey(Base64.encodeBase64String(((RSAPrivateKey) keyPair.getPrivate()).getEncoded()));
        return rsaKey;
    }

    /**
     * 公钥加密
     * 参考：@see com.alipay.api.internal.util.AlipaySignature.rsaEncrypt(String, String, String)
     * https://mvnrepository.com/artifact/com.alipay.sdk/alipay-sdk-java
     *
     * @param content   待加密内容
     * @param publicKey 公钥
     * @param charset   字符集，如UTF-8, GBK, GB2312
     * @return 密文内容
     * @throws RsaException
     */
    public static String rsaPublicEncrypt(String content, String publicKey, String charset) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(RSA_ALGORITHM, publicKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] data = StringUtils.isBlank(charset) ? content.getBytes() : content.getBytes(charset.trim());
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtils.isBlank(charset) ? new String(encryptedData) : new String(encryptedData, charset.trim());
        } catch (Exception e) {
            throw new RsaException("EncryptContent = " + content + ",charset = " + charset, e);
        }
    }

    //公钥解密
    public static String rsaPublicDecrypt(String content, String publicKey, String charset) {
        try {
            PublicKey pubKey = getPublicKeyFromX509(RSA_ALGORITHM, publicKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] encryptedData = StringUtils.isBlank(charset) ? Base64.decodeBase64(content.getBytes())
                    : Base64.decodeBase64(content.getBytes(charset.trim()));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtils.isBlank(charset) ? new String(decryptedData) : new String(decryptedData, charset.trim());
        } catch (Exception e) {
            throw new RsaException("EncryptContent = " + content + ",charset = " + charset, e);
        }
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(publicKey.getBytes())));
    }

    /**
     * 私钥解密
     * 参考：@see com.alipay.api.internal.util.AlipaySignature.rsaDecrypt(String, String, String)
     * https://mvnrepository.com/artifact/com.alipay.sdk/alipay-sdk-java
     *
     * @param content    待解密内容
     * @param privateKey 私钥
     * @param charset    字符集，如UTF-8, GBK, GB2312
     * @return 明文内容
     * @throws RsaException
     */
    public static String rsaPrivateDecrypt(String content, String privateKey, String charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(RSA_ALGORITHM, privateKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            byte[] encryptedData = StringUtils.isBlank(charset) ? Base64.decodeBase64(content.getBytes())
                    : Base64.decodeBase64(content.getBytes(charset.trim()));
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return StringUtils.isBlank(charset) ? new String(decryptedData) : new String(decryptedData, charset.trim());
        } catch (Exception e) {
            throw new RsaException("EncodeContent = " + content + ",charset = " + charset, e);
        }
    }

    //私钥加密
    public static String rsaPrivateEncrypt(String content, String privateKey, String charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8(RSA_ALGORITHM, privateKey);
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            byte[] data = StringUtils.isBlank(charset) ? content.getBytes() : content.getBytes(charset.trim());
            int inputLen = data.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = Base64.encodeBase64(out.toByteArray());
            out.close();
            return StringUtils.isBlank(charset) ? new String(encryptedData) : new String(encryptedData, charset.trim());
        } catch (Exception e) {
            throw new RsaException("EncodeContent = " + content + ",charset = " + charset, e);
        }
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, String privateKey) throws Exception {
        if (StringUtils.isBlank(privateKey) || StringUtils.isBlank(algorithm)) {
            return null;
        }
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes())));
    }

}
