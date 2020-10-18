package com.tz.redismanager.service.impl;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.service.ICacheInitCallbackService;
import com.tz.redismanager.service.ICacheService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * <p>缓存服务实现/p>
 *
 * @version 1.5.0
 * @time 2020-10-18 10:57
 **/
@Service
public class CacheServiceImpl implements ICacheService {

    private static final Logger logger = TraceLoggerFactory.getLogger(CacheServiceImpl.class);

    private static Map<String, LoadingCache> cacherMap = new ConcurrentHashMap<>();

    @Override
    public LoadingCache initCacher(String cacherKey, ICacheInitCallbackService initCallbackService) {
        if (cacherMap.containsKey(cacherKey)) {
            return cacherMap.get(cacherKey);
        }
        LoadingCache cacher = null;
        if (ConstInterface.Cacher.ANALYSIS_CACHER.equals(cacherKey)) {
            cacher = initAnalysisCacher(cacherKey, initCallbackService);
        }
        if (ConstInterface.Cacher.REDIS_CONFIG_CACHER.equals(cacherKey)) {
            cacher = initRedisConfigCacher(cacherKey, initCallbackService);
        }
        LoadingCache cacherTemp = cacherMap.putIfAbsent(cacherKey, cacher);
        logger.info("[本地缓存] [{}] [初始化完成]", cacherKey);
        return null != cacherTemp ? cacherTemp : cacher;
    }

    @Override
    public LoadingCache getCacher(String cacherKey) {
        return cacherMap.get(cacherKey);
    }

    @Override
    public void invalidateCache(String cacherKey, Object cacheKey) {
        LoadingCache cacher = this.getCacher(cacherKey);
        if (null == cacher) {
            return;
        }
        cacher.invalidate(cacheKey);
    }

    private LoadingCache initAnalysisCacher(String cacherKey, ICacheInitCallbackService callback) {
        return Caffeine.newBuilder()
                //写了之后多久过期
                .expireAfterWrite(30L, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(1000)
                .build((param) -> {
                    logger.info("[本地缓存] [{}] [回源查询] {param:{}}", cacherKey, JsonUtils.toJsonStr(param));
                    return callback.init(param);
                });
    }

    private LoadingCache initRedisConfigCacher(String cacherKey, ICacheInitCallbackService callback) {
        return Caffeine.newBuilder()
                //最后一次访问了之后多久过期
                .expireAfterAccess(1L, TimeUnit.HOURS)
                .initialCapacity(10)
                .maximumSize(1000)
                .build((id) -> {
                    logger.info("[本地缓存] [{}] [回源查询] {id:{}对应的redis连接信息}", cacherKey, id);
                    return callback.init(id);
                });
    }

}
