package com.tz.redismanager.security.token.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Token属性</p>
 *
 * @version 1.5.0
 * @time 2020-11-07 20:59
 **/
@ConfigurationProperties(prefix = "rm.token")
@Getter
@Setter
public class TokenProperties {

    /**
     * 过期时间(单位分钟)
     */
    private Integer expireTimeToMinutes = 12 * 60;

    /**
     * JWT配置
     */
    private JWTConfig jwt;

    @Getter
    @Setter
    public static class JWTConfig {

        /**
         * 签名key(Base64编码)
         */
        private String signKey;

        /**
         * payload是否开启加密
         */
        private Boolean payloadEncryptEnable = false;

        /**
         * payload使用AES加密的密钥(Base64编码)
         */
        private String payloadEncryptToAESKey;


    }

}
