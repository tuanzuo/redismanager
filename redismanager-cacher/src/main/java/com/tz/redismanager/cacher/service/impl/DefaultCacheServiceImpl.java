package com.tz.redismanager.cacher.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
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
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    private ExecutorService refreshCacheExecutor = new ThreadPoolExecutor(10, 20,
            500L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            //自定义线程名称方式1：使用guava中的ThreadFactoryBuilder
            //new ThreadFactoryBuilder().setNameFormat("RefreshCache-thread-%d").build(),
            //自定义线程名称方式2：使用spring的CustomizableThreadFactory
            new CustomizableThreadFactory("RefreshCache-thread-"),
            //自定义线程名称方式3：自定义ThreadFactory
            //new RefreshCacheThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy());

    /**
     * 自定义ThreadFactory：重新设置线程的名称
     *
     * @see Executors#defaultThreadFactory()
     */
    static class RefreshCacheThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        RefreshCacheThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            //重新设置线程的名称
            namePrefix = "RefreshCache-thread-" +
                    poolNumber.getAndIncrement();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

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
        Object result = null;
        String value = this.getCache(cacheable, cacheKey);
        if (StringUtils.isNotBlank(value)) {
            logger.info("[首次查询命中缓存] [{}]", cacheKey);
            result = CACHE_EMPTY_VALUE.equals(value) ? null : JsonUtils.parseObject(value, returnType);
        } else {
            result = this.setCache(cacheable, cacheKey, returnType, true, initCache);
        }
        //异步刷新缓存
        this.asyncRefreshCache(cacheable, cacheKey, returnType, initCache);
        return result;
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

    @Override
    public Map<String, LoadingCache<String, String>> getL1Cachers() {
        return l1CacherMap;
    }

    private String getCache(Cacheable cacheable, String cacheKey) {
        String value = null;
        if (cacheable.l1Cache().enable()) {
            /**当一级缓存不存在时如果二级缓存开启则会查询二级缓存-->{@link DefaultCacheServiceImpl#initL1Cacher}中LoadingCache.build时*/
            value = this.getL1Cache(cacheable, cacheKey);
        } else if (cacheable.l2Cache().enable()) {
            value = this.getL2Cache(cacheKey);
        }
        return value;
    }

    private String getL1Cache(Cacheable cacheable, String cacheKey) {
        logger.info("[查询一级缓存] [{}]", cacheKey);
        return this.getL1Cacher(cacheable).get(cacheKey);
    }

    private String getL2Cache(String cacheKey) {
        logger.info("[查询二级缓存] [{}]", cacheKey);
        return stringRedisTemplate.opsForValue().get(cacheKey);
    }

    private Long getL2CacheExpireTime(String cacheKey) {
        return stringRedisTemplate.getExpire(cacheKey);
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
        if (l1Cache.recordStats()) {
            //打开数据收集功能
            caffeine.recordStats();
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

    private Object setCache(Cacheable cacheable, String cacheKey, Type returnType, boolean reQueryCache, Function<Object, Object> initCache) {
        Object result = null;
        String lockKey = CACHE_LOCK_KEY_PRE + cacheKey;
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            //1、尝试加锁，最多等待200毫秒，加锁10秒后自动解锁
            if (!rLock.tryLock(200, 10 * 1000, TimeUnit.MILLISECONDS)) {
                throw new CacherException(ResultCode.CACHE_TRY_LOCK_WAIT_TIMEOUT);
            }
            if (reQueryCache) {
                //2、加锁成功，再次查询
                String value = this.getCache(cacheable, cacheKey);
                if (StringUtils.isNotBlank(value)) {
                    logger.info("[再次查询命中缓存] [{}]", cacheKey);
                    return CACHE_EMPTY_VALUE.equals(value) ? null : JsonUtils.parseObject(value, returnType);
                }
            }
            //3、回源查询
            result = initCache.apply(null);
            logger.info("[{}缓存器] [{}] [{}] [回源查询数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheable.key(), cacheable.name(), cacheKey);
            String json = null == result ? CACHE_EMPTY_VALUE : JsonUtils.toJsonStr(result);
            //4.1、数据设置到二级缓存
            if (cacheable.l2Cache().enable()) {
                this.setL2Cache(cacheable, cacheKey, json);
                logger.info("[{}缓存器] [{}] [{}] [初始化二级缓存数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheable.key(), cacheable.name(), cacheKey);
            }
            //4.2、数据设置到一级缓存
            if (cacheable.l1Cache().enable()) {
                this.setL1Cache(cacheable, cacheKey, json);
                logger.info("[{}缓存器] [{}] [{}] [初始化一级缓存数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheable.key(), cacheable.name(), cacheKey);
            }
        } catch (CacherException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[{}缓存器] [设置缓存异常] [{}]", !reQueryCache ? "异步刷新" : "", cacheKey, e);
            throw new CacherException(ResultCode.CACHE_LOCK_EXCEPTION);
        } finally {
            try {
                rLock.unlock();
            } catch (Exception e) {
                logger.warn("[{}缓存器] [解锁异常] lockKey:{}", !reQueryCache ? "异步刷新" : "", lockKey, e);
            }
        }
        return result;
    }

    private void setL1Cache(Cacheable cacheable, String cacheKey, String cacheValue) {
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

    /**
     * 异步刷新缓存
     */
    private void asyncRefreshCache(Cacheable cacheable, String cacheKey, Type returnType, Function<Object, Object> initCache) {
        //未开启异步刷新 或者 未开启二级缓存 或者 二级缓存的过期时间小于等于0 --> 不执行异步刷新缓存
        if (!cacheable.asyncRefresh() || !cacheable.l2Cache().enable() || cacheable.l2Cache().expireDuration() <= 0) {
            return;
        }
        //查询剩余过期时间
        Long ttl = this.getL2CacheExpireTime(cacheKey);
        //当剩余过期时间<=配置的过期时间的一半时才执行异步刷新缓存
        if (null != ttl && -1 != ttl && ttl <= (TimeoutUtils.toSeconds(cacheable.l2Cache().expireDuration(), cacheable.l2Cache().expireUnit()) >> 1)) {
            refreshCacheExecutor.execute(() -> {
                logger.info("[异步刷新缓存] [{}]", cacheKey);
                this.setCache(cacheable, cacheKey, returnType, false, initCache);
            });
        }
    }

}
