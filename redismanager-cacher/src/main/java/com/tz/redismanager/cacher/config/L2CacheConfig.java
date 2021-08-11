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

    /**
     * 是否开启二级缓存，true:开启，false:关闭
     */
    private boolean enable = true;

    /**
     * 过期时间
     */
    private long expireDuration = 10;

    /**
     * 过期时间单位
     */
    private TimeUnit expireUnit = TimeUnit.MINUTES;

}
