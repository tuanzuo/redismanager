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

    /**
     * 是否开启一级缓存，true:开启，false:关闭
     */
    private boolean enable = true;

    /**
     * 初始的缓存空间大小
     */
    private int initialCapacity = 100;

    /**
     * 缓存的最大条数
     */
    private long maximumSize = 1000;

    /**
     * 过期策略
     */
    private ExpireStrategy expireStrategy = ExpireStrategy.EXPIRE_AFTER_WRITE;

    /**
     * 过期时间
     */
    private long expireDuration = 2;

    /**
     * 过期时间单位
     */
    private TimeUnit expireUnit = TimeUnit.MINUTES;

    /**
     * 开启数据统计功能，true:开启，false:关闭
     */
    private boolean recordStats = true;
}
