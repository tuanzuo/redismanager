package com.tz.redismanager.cacher.service;

import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.config.CacheEvictConfig;
import com.tz.redismanager.cacher.config.CacheableConfig;

/**
 * <p>缓存器配置服务接口</p>
 *
 * @author admin
 * @version 1.0
 * @time 2021-03-29 22:15
 **/
public interface ICacherConfigService {

    void addCacheableConfig(CacheableConfig cacheableConfig);

    void addCacheEvictConfig(CacheEvictConfig cacheEvictConfig);

    CacheableConfig convertCacheable(Cacheable cacheable);

    CacheEvictConfig convertCacheEvict(CacheEvict cacheEvict);
}
