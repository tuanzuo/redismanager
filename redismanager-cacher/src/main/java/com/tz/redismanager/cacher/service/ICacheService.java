package com.tz.redismanager.cacher.service;

import com.tz.redismanager.cacher.annotation.Cacher;
import com.tz.redismanager.cacher.annotation.CacherEvict;

import java.lang.reflect.Type;
import java.util.function.Function;

/**
 * <p>缓存服务接口</p>
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

    /**
     * 得到缓存
     * @param cacher
     * @param cacheKey
     * @param returnType
     * @param initCache
     * @return
     */
    Object getCache(Cacher cacher, String cacheKey, Type returnType, Function<Object, Object> initCache);

    /**
     * 失效缓存
     *
     * @param cacheKey 缓存key
     */
    void invalidateCache(CacherEvict cacherEvict, String cacheKey);

    /**
     * 初始化缓存器
     * @param cacher
     */
    void initCacher(Cacher cacher);
}
