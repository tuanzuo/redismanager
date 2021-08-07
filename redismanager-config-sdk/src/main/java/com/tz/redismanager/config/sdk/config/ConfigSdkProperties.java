package com.tz.redismanager.config.sdk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>配置properties</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 20:42
 **/
@Configuration
@ConfigurationProperties(prefix = "rm.config")
@Getter
@Setter
public class ConfigSdkProperties {

    public static final String ZOOKEEPER = "zookeeper";
    public static final String ZOOKEEPER_CURATOR = "zookeeper_curator";

    public static final String MQ = "mq";
    public static final String RABBITMQ = "rabbitmq";

    /**
     * 配置同步类型
     */
    private String configSyncType = ZOOKEEPER;

    /**
     * 配置同步子类型
     */
    private String configSyncSubType = ZOOKEEPER_CURATOR;

}
