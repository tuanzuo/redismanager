package com.tz.redismanager.cacher.service;

import com.tz.redismanager.cacher.domain.Cacher;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-27 22:03
 **/
public interface ICacheService {

    /**
     * 判断是否支持缓存器类型
     *
     * @param cacherType 缓存器类型
     * @return true:支持,false:不支持
     */
    boolean support(String cacherType);

    Object getCache(Cacher cacher, String cacheKey, Type returnType, Function<Object, Object> initCache);

    /**
     * 失效缓存
     *
     * @param cacheKey 缓存key
     */
    void invalidateCache(Cacher cacher, String cacheKey);
}
