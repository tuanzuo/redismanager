package com.tz.redismanager.cacher.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.annotation.*;
import com.tz.redismanager.cacher.constant.ConstInterface;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * <p>默认的缓存服务实现</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-02 20:57
 **/
@Service
public class DefaultCacheServiceImpl implements ICacheService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultCacheServiceImpl.class);

    private static final String CACHE_LOCK_KEY_PRE = "rm:cache:lck:";

    private static final String CACHE_EMPTY_VALUE = "RM_CACHE_EMPTY_VALUE";

    private static Map<String, LoadingCache<String, String>> l1CacherMap = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public boolean support(String cacherType) {
        return ConstInterface.CacherType.DEFAULT_CACHER.equals(cacherType);
    }

    @Override
    public Object getCache(Cacheable cacheable, String cacheKey, Type returnType, Function<Object, Object> initCache) {
        String value = null;
        if (cacheable.l1Cache().enable()) {
            value = this.getL1Cache(cacheable, cacheKey, initCache);
        } else if (cacheable.l2Cache().enable()) {
            value = this.getL2Cache(cacheKey);
        }
        if (CACHE_EMPTY_VALUE.equals(value)) {
            return null;
        }
        if (StringUtils.isNotBlank(value)) {
            return JsonUtils.parseObject(value, returnType);
        }
        return this.setCache(cacheable, cacheKey, initCache);
    }

    @Override
    public void invalidateCache(CacheEvict cacheEvict, String cacheKey) {
        if (cacheEvict.l1Cache().enable()) {
            this.getL1Cacher(cacheEvict.key(), cacheEvict.l1Cache(), cacheEvict.l2Cache()).invalidate(cacheKey);
        }
        if (cacheEvict.l2Cache().enable()) {
            stringRedisTemplate.delete(cacheKey);
        }
    }

    @Override
    public void initCacher(Cacheable cacheable) {
        this.initL1Cacher(cacheable.key(), cacheable.l1Cache(), cacheable.l2Cache());
    }

    private String getL1Cache(Cacheable cacheable, String cacheKey, Function<Object, Object> initCache) {
        return this.getL1Cacher(cacheable).get(cacheKey);
    }

    private String getL2Cache(String cacheKey) {
        return stringRedisTemplate.opsForValue().get(cacheKey);
    }

    private LoadingCache<String, String> getL1Cacher(Cacheable cacheable) {
        return this.getL1Cacher(cacheable.key(), cacheable.l1Cache(), cacheable.l2Cache());
    }

    private LoadingCache<String, String> getL1Cacher(String cacherKey, L1Cache l1Cache, L2Cache l2Cache) {
        if (l1CacherMap.containsKey(cacherKey)) {
            return l1CacherMap.get(cacherKey);
        }
        return this.initL1Cacher(cacherKey, l1Cache, l2Cache);
    }

    private LoadingCache<String, String> initL1Cacher(String cacherKey, L1Cache l1Cache, L2Cache l2Cache) {
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
                /**
                 * 1、param的值等于{@link LoadingCache#get(Object)方法的入参}<br/>
                 * 2、当一级缓存的数据不存在时如果开启了二级缓存就查询二级缓存的数据返回，否则返回null<br/>
                 */
                .build((param) -> l2Cache.enable() ? this.getL2Cache(param) : null);
        return Optional.ofNullable(l1CacherMap.putIfAbsent(cacherKey, loadingCache)).orElse(loadingCache);
    }

    private Object setCache(Cacheable cacheable, String cacheKey, Function<Object, Object> initCache) {
        Object result = null;
        String lockKey = CACHE_LOCK_KEY_PRE + cacheKey;
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            // 尝试加锁，最多等待200毫秒，上锁以后10秒自动解锁
            if (!rLock.tryLock(200, 10 * 1000, TimeUnit.MILLISECONDS)) {
                throw new CacherException(ResultCode.CACHE_TRY_LOCK_WAIT_TIMEOUT);
            }
            result = initCache.apply(null);
            logger.info("[缓存器] [{}] [{}] [回源查询数据完成] [{}]", cacheable.key(), cacheable.name(), cacheKey);
            String json = null == result ? CACHE_EMPTY_VALUE : JsonUtils.toJsonStr(result);
            if (cacheable.l2Cache().enable()) {
                this.setL2Cache(cacheable, cacheKey, json);
                logger.info("[缓存器] [{}] [{}] [初始化二级缓存数据完成] [{}]", cacheable.key(), cacheable.name(), cacheKey);
            }
            if (cacheable.l1Cache().enable()) {
                this.setL1Cache(cacheable, cacheKey, initCache, json);
                logger.info("[缓存器] [{}] [{}] [初始化一级缓存数据完成] [{}]", cacheable.key(), cacheable.name(), cacheKey);
            }
        } catch (CacherException e) {
            throw e;
        } catch (Exception e) {
            throw new CacherException(ResultCode.CACHE_LOCK_EXCEPTION);
        } finally {
            try {
                rLock.unlock();
            } catch (Exception e) {
                logger.warn("[解锁异常] lockKey:{}", lockKey, e);
            }
        }
        return result;
    }

    private void setL1Cache(Cacheable cacheable, String cacheKey, Function<Object, Object> initCache, String cacheValue) {
        this.getL1Cacher(cacheable).put(cacheKey, cacheValue);
    }

    private void setL2Cache(Cacheable cacheable, String cacheKey, String cacheValue) {
        L2Cache l2Cache = cacheable.l2Cache();
        if (l2Cache.expireDuration() > 0) {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue, l2Cache.expireDuration(), l2Cache.expireUnit());
        } else {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue);
        }
    }

}
