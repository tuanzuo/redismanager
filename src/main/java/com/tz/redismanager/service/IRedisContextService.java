package com.tz.redismanager.service;


import org.springframework.data.redis.core.RedisTemplate;

public interface IRedisContextService {

    RedisTemplate<String, Object> initContext(String id);

    RedisTemplate<String, Object> getRedisTemplate(String id);
}
