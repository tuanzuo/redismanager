package com.tz.redismanager.cacher.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.constant.ConstInterface;
import com.tz.redismanager.cacher.domain.Cacher;
import com.tz.redismanager.cacher.domain.L1Cache;
import com.tz.redismanager.cacher.domain.L2Cache;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * <p></p>
 *
 * @author admin
 * @version 1.0
 * @time 2021-01-02 20:57
 **/
@Service
public class CacheServiceImpl implements ICacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private static final String CACHE_EMPTY_VALUE = "RM_CACHE_EMPTY_VALUE";

    private static Map<String, LoadingCache<String, String>> l1CacherMap = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean support(String cacherType) {
        return ConstInterface.CacherType.CACHER_DEFAULT.equals(cacherType);
    }

    @Override
    public Object getCache(Cacher cacher, String cacheKey, Type returnType, Function<Object, Object> initCache) {
        L1Cache l1Cache = cacher.l1Cache();
        L2Cache l2Cache = cacher.l2Cache();
        String value = null;
        if (l1Cache.enable()) {
            value = this.getL1Cache(cacher, cacheKey, initCache);
            if (StringUtils.isBlank(value) && l2Cache.enable()) {
                value = stringRedisTemplate.opsForValue().get(cacheKey);
            }
        } else if (l2Cache.enable()) {
            value = stringRedisTemplate.opsForValue().get(cacheKey);
        }
        if (CACHE_EMPTY_VALUE.equals(value)) {
            return null;
        }
        if (StringUtils.isBlank(value)) {
            Object result = initCache.apply(null);
            String json = null == result ? CACHE_EMPTY_VALUE : JsonUtils.toJsonStr(result);
            if (l1Cache.enable()) {
                this.getL1Cacher(cacher, initCache).put(cacheKey, json);
            }
            if (l2Cache.enable()) {
                stringRedisTemplate.opsForValue().set(cacheKey, json, l2Cache.expireDuration(), l2Cache.expireUnit());
            }
            return result;
        }
        return JsonUtils.parseObject(value, returnType);
    }

    @Override
    public void invalidateCache(Cacher cacher, String cacheKey) {
        L1Cache l1Cache = cacher.l1Cache();
        L2Cache l2Cache = cacher.l2Cache();
        if (l1Cache.enable()) {
            this.getL1Cacher(cacher, null).invalidate(cacheKey);
        }
        if (l2Cache.enable()) {
            stringRedisTemplate.delete(cacheKey);
        }
    }

    private String getL1Cache(Cacher cacher, String cacheKey, Function<Object, Object> initCache) {
        return this.getL1Cacher(cacher, initCache).get(cacheKey);
    }

    private LoadingCache<String, String> getL1Cacher(Cacher cacher, Function<Object, Object> initCache) {
        String cacherKey = cacher.key();
        L1Cache l1Cache = cacher.l1Cache();
        if (l1CacherMap.containsKey(cacherKey)) {
            return l1CacherMap.get(cacherKey);
        }
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        switch (l1Cache.expireStrategy()) {
            case EXPIRE_AFTER_ACCESS:
                caffeine.expireAfterAccess(l1Cache.expireDuration(), l1Cache.expireUnit());
                break;
            case EXPIRE_AFTER_WRITE:
                caffeine.expireAfterWrite(l1Cache.expireDuration(), l1Cache.expireUnit());
                break;
        }
        LoadingCache<String, String> loadingCache = caffeine.initialCapacity(l1Cache.initialCapacity())
                .maximumSize(l1Cache.maximumSize())
                .build((param) -> {
                    if (null == initCache) {
                        return null;
                    }
                    return JsonUtils.toJsonStr(initCache.apply(param));
                });
        LoadingCache<String, String> cacherTemp = l1CacherMap.putIfAbsent(cacherKey, loadingCache);
        return null != cacherTemp ? cacherTemp : loadingCache;
    }

}
