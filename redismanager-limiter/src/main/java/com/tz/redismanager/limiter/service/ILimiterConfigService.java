package com.tz.redismanager.limiter.service;

import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.config.LimiterConfig;

/**
 * <p>限流器配置接口</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-30 22:25
 **/
public interface ILimiterConfigService {

    /**
     * 添加限流器配置
     *
     * @param limiterConfig
     */
    void addLimiterConfig(LimiterConfig limiterConfig);

    /**
     * 转换成限流器配置
     *
     * @param limiter
     * @return
     */
    LimiterConfig convertLimiter(Limiter limiter);

}
