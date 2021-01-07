package com.tz.redismanager.cacher.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.constant.ConstInterface;
import com.tz.redismanager.cacher.domain.*;
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
 * <p>缓存服务实现</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-02 20:57
 **/
@Service
public class CacheServiceImpl implements ICacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private static final String CACHE_LOCK_KEY_PRE = "rm:cache:lck:";

    private static final String CACHE_EMPTY_VALUE = "RM_CACHE_EMPTY_VALUE";

    private static Map<String, LoadingCache<String, String>> l1CacherMap = new ConcurrentHashMap<>();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

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
                value = this.getL2Cache(cacher, cacheKey);
            }
        } else if (l2Cache.enable()) {
            value = this.getL2Cache(cacher, cacheKey);
        }
        if (CACHE_EMPTY_VALUE.equals(value)) {
            return null;
        }
        if (StringUtils.isNotBlank(value)) {
            return JsonUtils.parseObject(value, returnType);
        }
        return this.setCache(cacher, cacheKey, initCache);
    }

    private Object setCache(Cacher cacher, String cacheKey, Function<Object, Object> initCache) {
        Object result = null;
        String lockKey = CACHE_LOCK_KEY_PRE + cacheKey;
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            // 尝试加锁，最多等待200毫秒，上锁以后10秒自动解锁
            if (!rLock.tryLock(200, 10 * 1000, TimeUnit.MILLISECONDS)) {
                throw new CacherException(ResultCode.CACHE_TRY_LOCK_WAIT_TIMEOUT);
            }
            result = initCache.apply(null);
            String json = null == result ? CACHE_EMPTY_VALUE : JsonUtils.toJsonStr(result);
            if (cacher.l2Cache().enable()) {
                this.setL2Cache(cacher, cacheKey, json);
            }
            if (cacher.l1Cache().enable()) {
                this.setL1Cache(cacher, cacheKey, initCache, json);
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

    @Override
    public void invalidateCache(CacherEvict cacherEvict, String cacheKey) {
        if (cacherEvict.l1Cache().enable()) {
            this.getL1Cacher(cacherEvict.key(), cacherEvict.l1Cache(), cacherEvict.l2Cache(), cacheKey, null).invalidate(cacheKey);
        }
        if (cacherEvict.l2Cache().enable()) {
            stringRedisTemplate.delete(cacheKey);
        }
    }

    private String getL1Cache(Cacher cacher, String cacheKey, Function<Object, Object> initCache) {
        return this.getL1Cacher(cacher, cacheKey, initCache).get(cacheKey);
    }

    private String getL2Cache(Cacher cacher, String cacheKey) {
        return stringRedisTemplate.opsForValue().get(cacheKey);
    }

    private void setL1Cache(Cacher cacher, String cacheKey, Function<Object, Object> initCache, String cacheValue) {
        this.getL1Cacher(cacher, cacheKey, initCache).put(cacheKey, cacheValue);
    }

    private void setL2Cache(Cacher cacher, String cacheKey, String cacheValue) {
        L2Cache l2Cache = cacher.l2Cache();
        if (l2Cache.expireDuration() > 0) {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue, l2Cache.expireDuration(), l2Cache.expireUnit());
        } else {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue);
        }
    }

    private LoadingCache<String, String> getL1Cacher(Cacher cacher, String cacheKey, Function<Object, Object> initCache) {
        return this.getL1Cacher(cacher.key(), cacher.l1Cache(), cacher.l2Cache(), cacheKey, initCache);
    }

    private LoadingCache<String, String> getL1Cacher(String cacherKey, L1Cache l1Cache, L2Cache l2Cache, String cacheKey, Function<Object, Object> initCache) {
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
        LoadingCache<String, String> loadingCache =
                caffeine.initialCapacity(l1Cache.initialCapacity())
                        .maximumSize(l1Cache.maximumSize())
                        /**param的值等于{@link com.github.benmanes.caffeine.cache.LoadingCache#get(java.lang.Object)方法的入参}*/
                        .build((param) -> {
                            if (l2Cache.enable()) {
                                return stringRedisTemplate.opsForValue().get(param);
                            }
                            if (null == initCache) {
                                return null;
                            }
                            return JsonUtils.toJsonStr(initCache.apply(param));
                        });
        return Optional.ofNullable(l1CacherMap.putIfAbsent(cacherKey, loadingCache)).orElse(loadingCache);
    }

}
