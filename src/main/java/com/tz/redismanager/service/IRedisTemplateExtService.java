package com.tz.redismanager.service;

/**
 * <p>RedisTemplate扩展服务接口</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:53
 **/
public interface IRedisTemplateExtService {

    Long bitCount(String key);
}
