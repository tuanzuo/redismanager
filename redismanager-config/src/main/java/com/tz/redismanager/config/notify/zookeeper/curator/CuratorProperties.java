package com.tz.redismanager.config.notify.zookeeper.curator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>curator配置properties</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 20:42
 **/
@ConfigurationProperties(prefix = "rm.config.curator")
@Getter
@Setter
public class CuratorProperties {

    private Integer baseSleepTimeMs = 1000;

    private Integer maxRetries = 10;
}
