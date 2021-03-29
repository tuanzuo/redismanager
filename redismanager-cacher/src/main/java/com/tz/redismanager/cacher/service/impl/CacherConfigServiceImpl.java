package com.tz.redismanager.cacher.service.impl;

import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.cacher.config.CacherConfig;
import com.tz.redismanager.cacher.config.L1CacheConfig;
import com.tz.redismanager.cacher.config.L2CacheConfig;
import com.tz.redismanager.cacher.service.ICacherConfigService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>缓存器配置服务实现</p>
 *
 * @author admin
 * @version 1.0
 * @time 2021-03-29 22:15
 **/
public class CacherConfigServiceImpl implements ICacherConfigService {

    private Map<String, CacherConfig> cachers = new ConcurrentHashMap<>();

    @Override
    public void add(CacherConfig cacherConfig) {
        cachers.put(cacherConfig.getKey(), cacherConfig);
    }

    @Override
    public CacherConfig get(String cacherKey) {
        return cachers.get(cacherKey);
    }

    @Override
    public CacherConfig convertCacheable(Cacheable cacheable) {
        return this.getCacherConfig(cacheable.name(), cacheable.key(), cacheable.asyncRefresh(), cacheable.l1Cache(), cacheable.l2Cache());
    }

    @Override
    public CacherConfig convertCacheEvict(CacheEvict cacheEvict) {
        return this.getCacherConfig(cacheEvict.name(), cacheEvict.key(), cacheEvict.asyncRefresh(), cacheEvict.l1Cache(), cacheEvict.l2Cache());
    }

    private CacherConfig getCacherConfig(String name, String key, boolean asyncRefresh, L1Cache l1Cache, L2Cache l2Cache) {
        CacherConfig.Builder configBuilder = CacherConfig.newBuilder();
        configBuilder.setName(name);
        configBuilder.setKey(key);
        configBuilder.setAsyncRefresh(asyncRefresh);
        L1CacheConfig l1CacheConfig = new L1CacheConfig();
        l1CacheConfig.setEnable(l1Cache.enable());
        l1CacheConfig.setInitialCapacity(l1Cache.initialCapacity());
        l1CacheConfig.setMaximumSize(l1Cache.maximumSize());
        l1CacheConfig.setExpireStrategy(l1Cache.expireStrategy());
        l1CacheConfig.setExpireDuration(l1Cache.expireDuration());
        l1CacheConfig.setExpireUnit(l1Cache.expireUnit());
        l1CacheConfig.setRecordStats(l1Cache.recordStats());
        configBuilder.setL1Cache(l1CacheConfig);
        L2CacheConfig l2CacheConfig = new L2CacheConfig();
        l2CacheConfig.setEnable(l2Cache.enable());
        l2CacheConfig.setExpireDuration(l2Cache.expireDuration());
        l2CacheConfig.setExpireUnit(l2Cache.expireUnit());
        configBuilder.setL2Cache(l2CacheConfig);
        return configBuilder.build();
    }
}
