package com.tz.redismanager.limiter.service;

import com.tz.redismanager.limiter.annotation.Limiter;

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
     * @param limiter
     * @return true:获取成功,false:获取失败
     */
    boolean tryAcquire(Limiter limiter);

    /**
     * 初始化限流器
     *
     * @param limiter
     */
    void initLimiter(Limiter limiter);

}
