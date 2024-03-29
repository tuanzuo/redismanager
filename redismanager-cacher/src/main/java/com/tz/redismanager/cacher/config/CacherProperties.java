package com.tz.redismanager.cacher.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>缓存器配置Properties</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 20:46
 **/
@ConfigurationProperties(prefix = "rm")
@Getter
@Setter
public class CacherProperties {

    /**
     * 缓存生效配置
     */
    private Map<String, CacheableConfig> cacheables = new HashMap<>();

    /**
     * 缓存失效配置
     */
    private Map<String, CacheEvictConfig> cacheEvicts = new HashMap<>();

}
