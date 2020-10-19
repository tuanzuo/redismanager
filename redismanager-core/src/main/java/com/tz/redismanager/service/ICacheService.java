package com.tz.redismanager.service;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.constant.ConstInterface;

/**
 * <p>缓存服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-10-18 10:56
 **/
public interface ICacheService {

    /**
     * 初始化缓存器
     * @param cacherKey 缓存器key {@link ConstInterface.Cacher}
     * @param initCallbackService 初始化缓存器回调方法
     * @return
     */
    LoadingCache initCacher(String cacherKey, ICacheInitCallbackService initCallbackService);

    /**
     * 得到缓存器
     * @param cacherKey 缓存器key {@link ConstInterface.Cacher}
     * @return
     */
    LoadingCache getCacher(String cacherKey);

    /**
     * 失效缓存
     * @param cacherKey 缓存器key {@link ConstInterface.Cacher}
     * @param param 缓存key
     */
    void invalidateCache(String cacherKey, Object cacheKey);

}
