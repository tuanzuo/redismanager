package com.tz.redismanager.config.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 **/
@ConfigurationProperties(prefix = "rm.config")
@Getter
@Setter
public class ConfigProperties {

    private String configTableName = "t_config";

}
