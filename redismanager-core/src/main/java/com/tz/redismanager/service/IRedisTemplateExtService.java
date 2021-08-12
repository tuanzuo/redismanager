package com.tz.redismanager.service;

/**
 * <p>RedisTemplate扩展服务接口</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-12 22:53
 **/
public interface IRedisTemplateExtService {

    /**
     * 计算给定字符串key中，被设置为 1 的比特位的数量
     * @param key
     * @return
     */
    Long bitCount(String key);
}
