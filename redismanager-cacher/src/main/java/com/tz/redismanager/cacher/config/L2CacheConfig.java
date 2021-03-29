package com.tz.redismanager.cacher.config;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * <p>二级缓存配置</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 22:17
 **/
@Getter
@Setter
public class L2CacheConfig {

    private boolean enable = true;

    private long expireDuration = 10;

    private TimeUnit expireUnit = TimeUnit.MINUTES;
}
