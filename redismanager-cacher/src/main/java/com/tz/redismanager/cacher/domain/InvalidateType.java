package com.tz.redismanager.cacher.domain;

/**
 * <p>失效类型</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-04-01 0:19
 **/
public enum InvalidateType {
    //失效一级缓存
    L1,
    //失效二级缓存
    L2,
    //失效所有缓存
    ALL,
    ;
}
