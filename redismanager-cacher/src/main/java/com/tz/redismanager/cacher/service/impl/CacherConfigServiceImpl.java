package com.tz.redismanager.cacher.service.impl;

import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.cacher.config.CacheEvictConfig;
import com.tz.redismanager.cacher.config.CacheableConfig;
import com.tz.redismanager.cacher.config.L1CacheConfig;
import com.tz.redismanager.cacher.config.L2CacheConfig;
import com.tz.redismanager.cacher.service.ICacherConfigService;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>缓存器配置服务实现</p>
 *
 * @author admin
 * @version 1.0
 * @time 2021-03-29 22:15
 **/
public class CacherConfigServiceImpl implements ICacherConfigService {

    private Map<String, CacheableConfig> cacheableConfigMap = new ConcurrentHashMap<>();
    private Map<String, CacheEvictConfig> cacheEvictConfigMap = new ConcurrentHashMap<>();

    @Override
    public void addCacheableConfig(CacheableConfig cacheableConfig) {
        cacheableConfigMap.put(cacheableConfig.getKey(), cacheableConfig);
    }

    @Override
    public void addCacheEvictConfig(CacheEvictConfig cacheEvictConfig) {
        cacheEvictConfigMap.put(cacheEvictConfig.getKey(), cacheEvictConfig);
    }

    @Override
    public CacheableConfig getCacheableConfig(String cacherKey) {
        return cacheableConfigMap.get(cacherKey);
    }

    @Override
    public CacheEvictConfig getCacheEvictConfig(String cacherKey) {
        return cacheEvictConfigMap.get(cacherKey);
    }

    @Override
    public CacheableConfig convertCacheable(Cacheable cacheable) {
        return this.getCacheableConfig(cacheable);
    }

    @Override
    public CacheEvictConfig convertCacheEvict(CacheEvict cacheEvict) {
        return this.getCacheEvictConfig(cacheEvict);
    }

    private CacheableConfig getCacheableConfig(Cacheable cacheable) {
        return Optional.ofNullable(cacheableConfigMap.get(cacheable.key())).orElseGet(() -> {
            CacheableConfig.Builder builder = CacheableConfig.newBuilder();
            builder.setName(cacheable.name());
            builder.setKey(cacheable.key());
            builder.setAsyncRefresh(cacheable.asyncRefresh());
            L1CacheConfig l1CacheConfig = new L1CacheConfig();
            L1Cache l1Cache = cacheable.l1Cache();
            l1CacheConfig.setEnable(l1Cache.enable());
            l1CacheConfig.setInitialCapacity(l1Cache.initialCapacity());
            l1CacheConfig.setMaximumSize(l1Cache.maximumSize());
            l1CacheConfig.setExpireStrategy(l1Cache.expireStrategy());
            l1CacheConfig.setExpireDuration(l1Cache.expireDuration());
            l1CacheConfig.setExpireUnit(l1Cache.expireUnit());
            l1CacheConfig.setRecordStats(l1Cache.recordStats());
            builder.setL1Cache(l1CacheConfig);
            L2CacheConfig l2CacheConfig = new L2CacheConfig();
            L2Cache l2Cache = cacheable.l2Cache();
            l2CacheConfig.setEnable(l2Cache.enable());
            l2CacheConfig.setExpireDuration(l2Cache.expireDuration());
            l2CacheConfig.setExpireUnit(l2Cache.expireUnit());
            builder.setL2Cache(l2CacheConfig);
            CacheableConfig config = builder.build();
            return Optional.ofNullable(cacheableConfigMap.putIfAbsent(config.getKey(), config)).orElse(config);
        });

    }

    private CacheEvictConfig getCacheEvictConfig(CacheEvict cacheEvict) {
        return Optional.ofNullable(cacheEvictConfigMap.get(cacheEvict.key())).orElseGet(() -> {
            CacheEvictConfig.Builder builder = CacheEvictConfig.newBuilder();
            builder.setName(cacheEvict.name());
            builder.setKey(cacheEvict.key());
            builder.setInvalidate(cacheEvict.invalidate());
            builder.setInvocation(cacheEvict.invocation());
            CacheEvictConfig config = builder.build();
            return Optional.ofNullable(cacheEvictConfigMap.putIfAbsent(config.getKey(), config)).orElse(config);
        });
    }

}
