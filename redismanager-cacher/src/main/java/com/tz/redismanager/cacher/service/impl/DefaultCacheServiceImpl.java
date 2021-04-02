package com.tz.redismanager.cacher.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.cacher.config.CacheEvictConfig;
import com.tz.redismanager.cacher.config.CacheableConfig;
import com.tz.redismanager.cacher.config.L1CacheConfig;
import com.tz.redismanager.cacher.config.L2CacheConfig;
import com.tz.redismanager.cacher.constant.ConstInterface;
import com.tz.redismanager.cacher.domain.InvalidateType;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
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
import java.util.*;
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
    public Object getCache(CacheableConfig cacheableConfig, String cacheKey, Type returnType, Function<Object, Object> initCache) {
        Object result = null;
        String value = this.getCache(cacheableConfig, cacheKey);
        if (StringUtils.isNotBlank(value)) {
            logger.info("[首次查询命中缓存] [{}]", cacheKey);
            result = CACHE_EMPTY_VALUE.equals(value) ? null : JsonUtils.parseObject(value, returnType);
        } else {
            result = this.setCache(cacheableConfig, cacheKey, returnType, true, initCache);
        }
        //异步刷新缓存
        this.asyncRefreshCache(cacheableConfig, cacheKey, returnType, initCache);
        return result;
    }

    @Override
    public void invalidateCache(CacheEvictConfig cacheEvictConfig, String cacheKey) {
        InvalidateType invalidate = cacheEvictConfig.getInvalidate();
        if (InvalidateType.ALL == invalidate || InvalidateType.L1 == invalidate) {
            LoadingCache<String, String> loadingCache = this.getL1Cacher(cacheEvictConfig.getKey());
            if (null != loadingCache) {
                loadingCache.invalidate(cacheKey);
            }
        }
        if (InvalidateType.ALL == invalidate || InvalidateType.L2 == invalidate) {
            stringRedisTemplate.delete(cacheKey);
        }
    }

    @Override
    public void initCacher(CacheableConfig cacheableConfig) {
        this.initL1Cacher(cacheableConfig.getKey(), cacheableConfig.getL1Cache(), cacheableConfig.getL2Cache());
    }

    @Override
    public void resetCacher(CacheableConfig cacheableConfig) {
        LoadingCache<String, String> oldLoadingCache = l1CacherMap.get(cacheableConfig.getKey());
        if (null != oldLoadingCache) {
            oldLoadingCache.invalidateAll();
            oldLoadingCache = null;
        }
        LoadingCache<String, String> loadingCache = this.createL1Cacher(cacheableConfig.getL1Cache(), cacheableConfig.getL2Cache());
        l1CacherMap.put(cacheableConfig.getKey(), loadingCache);
    }

    @Override
    public Map<String, Object> getL1CacherInfo(String cacherName) {
        Map<String, Object> result = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(cacherName)) {
            LoadingCache<String, String> l1Cache = l1CacherMap.get(cacherName);
            if (null == l1Cache) {
                return result;
            }
            result.put("name", cacherName);
            result.put("estimatedSize", l1Cache.estimatedSize());
            result.put("cacheStats", this.buildCacheStats(l1Cache));
            return result;
        }
        result.put("l1CacherCount", l1CacherMap.size());
        List<CacherData> details = new ArrayList<>();
        l1CacherMap.forEach((key, value) -> {
            CacherData data = new CacherData();
            data.setName(key);
            data.setEstimatedSize(l1CacherMap.get(key).estimatedSize());
            details.add(data);
        });
        result.put("details", details);
        return result;
    }

    private String getCache(CacheableConfig cacheableConfig, String cacheKey) {
        String value = null;
        if (cacheableConfig.getL1Cache().isEnable()) {
            /**当一级缓存不存在时如果二级缓存开启则会查询二级缓存-->{@link DefaultCacheServiceImpl#initL1Cacher}中LoadingCache.build时*/
            value = this.getL1Cache(cacheableConfig, cacheKey);
        } else if (cacheableConfig.getL2Cache().isEnable()) {
            value = this.getL2Cache(cacheKey);
        }
        return value;
    }

    private String getL1Cache(CacheableConfig cacheableConfig, String cacheKey) {
        logger.info("[查询一级缓存] [{}]", cacheKey);
        return this.getOrInitL1Cacher(cacheableConfig).get(cacheKey);
    }

    private String getL2Cache(String cacheKey) {
        logger.info("[查询二级缓存] [{}]", cacheKey);
        return stringRedisTemplate.opsForValue().get(cacheKey);
    }

    private Long getL2CacheExpireTime(String cacheKey) {
        return stringRedisTemplate.getExpire(cacheKey);
    }

    private LoadingCache<String, String> getOrInitL1Cacher(CacheableConfig cacheableConfig) {
        return this.getOrInitL1Cacher(cacheableConfig.getKey(), cacheableConfig.getL1Cache(), cacheableConfig.getL2Cache());
    }

    private LoadingCache<String, String> getOrInitL1Cacher(String cacherKey, L1CacheConfig l1Cache, L2CacheConfig l2Cache) {
        if (l1CacherMap.containsKey(cacherKey)) {
            return l1CacherMap.get(cacherKey);
        }
        return this.initL1Cacher(cacherKey, l1Cache, l2Cache);
    }

    private LoadingCache<String, String> getL1Cacher(String cacherKey) {
        return l1CacherMap.get(cacherKey);
    }

    private LoadingCache<String, String> initL1Cacher(String cacherKey, L1CacheConfig l1Cache, L2CacheConfig l2Cache) {
        LoadingCache<String, String> loadingCache = this.createL1Cacher(l1Cache, l2Cache);
        return Optional.ofNullable(l1CacherMap.putIfAbsent(cacherKey, loadingCache)).orElse(loadingCache);
    }

    private LoadingCache<String, String> createL1Cacher(L1CacheConfig l1Cache, L2CacheConfig l2Cache) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        switch (l1Cache.getExpireStrategy()) {
            case EXPIRE_AFTER_ACCESS:
                caffeine.expireAfterAccess(l1Cache.getExpireDuration(), l1Cache.getExpireUnit());
                break;
            case EXPIRE_AFTER_WRITE:
                caffeine.expireAfterWrite(l1Cache.getExpireDuration(), l1Cache.getExpireUnit());
                break;
        }
        if (l1Cache.isRecordStats()) {
            //打开数据收集功能
            caffeine.recordStats();
        }
        return caffeine.initialCapacity(l1Cache.getInitialCapacity())
                .maximumSize(l1Cache.getMaximumSize())
                /**
                 * 1、param的值等于{@link LoadingCache#get(Object)方法的入参}<br/>
                 * 2、当一级缓存的数据不存在时如果开启了二级缓存就查询二级缓存的数据返回，否则返回null<br/>
                 */
                .build((param) -> l2Cache.isEnable() ? this.getL2Cache(param) : null);
    }

    private Object setCache(CacheableConfig cacheableConfig, String cacheKey, Type returnType, boolean reQueryCache, Function<Object, Object> initCache) {
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
                String value = this.getCache(cacheableConfig, cacheKey);
                if (StringUtils.isNotBlank(value)) {
                    logger.info("[再次查询命中缓存] [{}]", cacheKey);
                    return CACHE_EMPTY_VALUE.equals(value) ? null : JsonUtils.parseObject(value, returnType);
                }
            }
            //3、回源查询
            result = initCache.apply(null);
            logger.info("[{}缓存器] [{}] [{}] [回源查询数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheableConfig.getKey(), cacheableConfig.getName(), cacheKey);
            String json = null == result ? CACHE_EMPTY_VALUE : JsonUtils.toJsonStr(result);
            //4.1、数据设置到二级缓存
            if (cacheableConfig.getL2Cache().isEnable()) {
                this.setL2Cache(cacheableConfig, cacheKey, json);
                logger.info("[{}缓存器] [{}] [{}] [初始化二级缓存数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheableConfig.getKey(), cacheableConfig.getName(), cacheKey);
            }
            //4.2、数据设置到一级缓存
            if (cacheableConfig.getL1Cache().isEnable()) {
                this.setL1Cache(cacheableConfig, cacheKey, json);
                logger.info("[{}缓存器] [{}] [{}] [初始化一级缓存数据完成] [{}]", !reQueryCache ? "异步刷新" : "", cacheableConfig.getKey(), cacheableConfig.getName(), cacheKey);
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

    private void setL1Cache(CacheableConfig cacheableConfig, String cacheKey, String cacheValue) {
        this.getOrInitL1Cacher(cacheableConfig).put(cacheKey, cacheValue);
    }

    private void setL2Cache(CacheableConfig cacheableConfig, String cacheKey, String cacheValue) {
        L2CacheConfig l2Cache = cacheableConfig.getL2Cache();
        if (l2Cache.getExpireDuration() > 0) {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue, l2Cache.getExpireDuration(), l2Cache.getExpireUnit());
        } else {
            stringRedisTemplate.opsForValue().set(cacheKey, cacheValue);
        }
    }

    /**
     * 异步刷新缓存
     */
    private void asyncRefreshCache(CacheableConfig cacheableConfig, String cacheKey, Type returnType, Function<Object, Object> initCache) {
        //未开启异步刷新 或者 未开启二级缓存 或者 二级缓存的过期时间小于等于0 --> 不执行异步刷新缓存
        if (!cacheableConfig.isAsyncRefresh() || !cacheableConfig.getL2Cache().isEnable() || cacheableConfig.getL2Cache().getExpireDuration() <= 0) {
            return;
        }
        //查询剩余过期时间
        Long ttl = this.getL2CacheExpireTime(cacheKey);
        //当剩余过期时间<=配置的过期时间的一半时才执行异步刷新缓存
        if (null != ttl && -1 != ttl && ttl <= (TimeoutUtils.toSeconds(cacheableConfig.getL2Cache().getExpireDuration(), cacheableConfig.getL2Cache().getExpireUnit()) >> 1)) {
            refreshCacheExecutor.execute(() -> {
                logger.info("[异步刷新缓存] [{}]", cacheKey);
                this.setCache(cacheableConfig, cacheKey, returnType, false, initCache);
            });
        }
    }

    private CacheStats buildCacheStats(LoadingCache<String, String> l1Cache) {
        com.github.benmanes.caffeine.cache.stats.@NonNull CacheStats stats = l1Cache.stats();
        CacheStats cacheStats = new CacheStats();
        cacheStats.setHitCount(stats.hitCount());
        cacheStats.setMissCount(stats.missCount());
        cacheStats.setHitRate(stats.hitRate());
        cacheStats.setMissRate(stats.missRate());
        cacheStats.setLoadSuccessCount(stats.loadSuccessCount());
        cacheStats.setLoadFailureCount(stats.loadFailureCount());
        cacheStats.setTotalLoadTime(stats.totalLoadTime());
        cacheStats.setEvictionCount(stats.evictionCount());
        cacheStats.setEvictionWeight(stats.evictionWeight());
        return cacheStats;
    }

    @Getter
    @Setter
    public static class CacherData {
        private String name;
        private Long estimatedSize;
    }

    @Getter
    @Setter
    public static class CacheStats {
        private long hitCount;
        private long missCount;
        private double missRate;
        private double hitRate;
        private long loadSuccessCount;
        private long loadFailureCount;
        private long totalLoadTime;
        private long evictionCount;
        private long evictionWeight;
    }

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

}
