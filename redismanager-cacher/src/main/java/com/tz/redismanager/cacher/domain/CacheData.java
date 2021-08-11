package com.tz.redismanager.cacher.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>缓存数据对象</p>
 *
 * @author tuanzou
 * @version 1.6.0
 * @time 2021-01-11 21:08
 **/
@Setter
@Getter
public class CacheData {
    /**
     * 缓存类型，一级缓存：L1，二级缓存：L2
     */
    private String type;

    /**
     * 缓存数据
     */
    private String data;

}
