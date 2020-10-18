package com.tz.redismanager.service;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * <p>缓存服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-10-18 10:56
 **/
public interface ICacheService {

    LoadingCache initCacher(String cacherKey, ICacheInitCallbackService initCallbackService);

    LoadingCache getCacher(String cacherKey);

}
