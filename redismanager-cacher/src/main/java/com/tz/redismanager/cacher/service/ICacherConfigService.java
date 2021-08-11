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

    /**
     * 添加缓存生效配置
     *
     * @param cacheableConfig
     */
    void addCacheableConfig(CacheableConfig cacheableConfig);

    /**
     * 添加缓存失效配置
     *
     * @param cacheEvictConfig
     */
    void addCacheEvictConfig(CacheEvictConfig cacheEvictConfig);

    /**
     * 转换成缓存生效配置
     *
     * @param cacheable
     * @return
     */
    CacheableConfig convertCacheable(Cacheable cacheable);

    /**
     * 转换成缓存失效配置
     *
     * @param cacheEvict
     * @return
     */
    CacheEvictConfig convertCacheEvict(CacheEvict cacheEvict);
}
