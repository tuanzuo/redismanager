package com.tz.redismanager.cacher.service;

import com.tz.redismanager.cacher.config.CacherConfig;

import java.lang.reflect.Type;
import java.util.Map;
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
     * @param cacherConfig
     * @param cacheKey
     * @param returnType
     * @param initCache
     * @return
     */
    Object getCache(CacherConfig cacherConfig, String cacheKey, Type returnType, Function<Object, Object> initCache);

    /**
     * 失效缓存
     *
     * @param cacheKey 缓存key
     */
    void invalidateCache(CacherConfig cacherConfig, String cacheKey);

    /**
     * 初始化缓存器
     * @param cacherConfig
     */
    void initCacher(CacherConfig cacherConfig);

    /**
     * 得到一级缓存器信息
     * @param cacherName
     * @return
     */
    Map<String, Object> getL1CacherInfo(String cacherName);

}
