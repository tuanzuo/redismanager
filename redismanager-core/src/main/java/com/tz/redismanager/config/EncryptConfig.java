package com.tz.redismanager.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 加密配置
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:24:16
 * @Version:1.1.0
 */
@Configuration
@ConfigurationProperties(prefix = "rm.encrypt")
@Setter
@Getter
@ToString
public class EncryptConfig {

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * md5加盐
     */
    private String md5Salt;

}
