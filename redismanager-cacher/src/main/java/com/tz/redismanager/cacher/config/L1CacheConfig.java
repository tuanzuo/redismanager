package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.domain.ExpireStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * <p>一级缓存配置</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 22:17
 **/
@Getter
@Setter
public class L1CacheConfig {

    private boolean enable = true;

    private int initialCapacity = 100;

    private long maximumSize = 1000;

    private ExpireStrategy expireStrategy = ExpireStrategy.EXPIRE_AFTER_WRITE;

    private long expireDuration = 2;

    private TimeUnit expireUnit = TimeUnit.MINUTES;

    private boolean recordStats = true;
}
