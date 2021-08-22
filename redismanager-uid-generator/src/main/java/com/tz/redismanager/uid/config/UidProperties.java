package com.tz.redismanager.uid.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>uid配置属性</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-22 22:58
 **/
@ConfigurationProperties(prefix = "rm.uid")
@Configuration
@Getter
@Setter
public class UidProperties {

    private int timeBits = 31;
    private int workerBits = 21;
    private int seqBits = 11;
    private String epochStr = "2021-08-22";

}
