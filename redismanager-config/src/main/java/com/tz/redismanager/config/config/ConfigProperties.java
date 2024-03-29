package com.tz.redismanager.config.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>配置Properties</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 **/
@Configuration
@ConfigurationProperties(prefix = "rm.config")
@Getter
@Setter
public class ConfigProperties {

    public static final String ZOOKEEPER = "zookeeper";
    public static final String ZOOKEEPER_CURATOR = "zookeeper_curator";

    public static final String MQ = "mq";
    public static final String RABBITMQ = "rabbitmq";

    /**
     * 配置保存的表名
     */
    private String configTableName = "t_config";

    /**
     * 配置同步类型
     */
    private String configSyncType = ZOOKEEPER;

    /**
     * 配置同步子类型
     */
    private String configSyncSubType = ZOOKEEPER_CURATOR;

}
