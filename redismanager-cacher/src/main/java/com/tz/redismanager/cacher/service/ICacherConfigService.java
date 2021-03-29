package com.tz.redismanager.cacher.service;

import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.config.CacherConfig;

/**
 * <p>缓存器配置服务接口</p>
 *
 * @author admin
 * @version 1.0
 * @time 2021-03-29 22:15
 **/
public interface ICacherConfigService {

    void add(CacherConfig cacherConfig);

    CacherConfig get(String cacherKey);

    CacherConfig convertCacheable(Cacheable cacheable);

    CacherConfig convertCacheEvict(CacheEvict cacheEvict);
}
