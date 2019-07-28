package com.tz.redismanager.service;


import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.bean.po.RedisConfigPO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public interface IRedisContextService {

    RedisTemplate<String, Object> initContext(String id);

    RedisTemplate<String, Object> getRedisTemplate(String id);

    Map<String, RedisTemplate<String, Object>> getRedisTemplateMap();

    void removeRedisTemplate(String id);

    LoadingCache<String, RedisConfigPO> getRedisConfigCache();
}
