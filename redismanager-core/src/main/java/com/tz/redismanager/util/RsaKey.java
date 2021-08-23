package com.tz.redismanager.util;

/**
 * RSA的key
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:53:57
 * @Version:1.1.0
 */
public class RsaKey {
    /**
     * 公钥(Base64加密过的)
     */
    private String publicKey;
    /**
     * 私钥(Base64加密过的)
     */
    private String privateKey;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }


}