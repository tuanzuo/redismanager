package com.tz.redismanager.cacher.domain;

/**
 * <p>执行策略</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-10 20:42
 **/
public enum InvocationStrategy {
    //方法执行之前
    BEFORE(),
    //方法执行之后
    AFTER(),
    ;
}
