package com.tz.redismanager.limiter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>限流器Properties</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-30 21:53
 **/
@ConfigurationProperties(prefix = "rm")
@Getter
@Setter
public class LimiterProperties {

    private Map<String, LimiterConfig> limiters = new HashMap<>();

}
