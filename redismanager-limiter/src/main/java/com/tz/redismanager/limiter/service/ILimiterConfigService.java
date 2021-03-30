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

    void add(LimiterConfig limiterConfig);

    LimiterConfig get(String limiterKey);

    LimiterConfig convertLimiter(Limiter limiter);

}
