package com.tz.redismanager.limiter.service;

import com.tz.redismanager.limiter.config.LimiterConfig;

/**
 * <p>限流器接口</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-21 22:11
 **/
public interface ILimiterService {

    /**
     * 判断是否支持限流器类型
     *
     * @param limiterType 限流器类型
     * @return true:支持,false:不支持
     */
    boolean support(String limiterType);

    /**
     * 尝试获得许可
     *
     * @param limiterConfig
     * @return true:获取成功,false:获取失败
     */
    boolean tryAcquire(LimiterConfig limiterConfig);

    /**
     * 初始化限流器
     *
     * @param limiterConfig
     */
    void initLimiter(LimiterConfig limiterConfig);

    /**
     * 重置限流器
     *
     * @param limiterConfig
     */
    void resetLimiter(LimiterConfig limiterConfig);

}
