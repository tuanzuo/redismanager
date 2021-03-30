package com.tz.redismanager.limiter.service.impl;

import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.config.LimiterConfig;
import com.tz.redismanager.limiter.service.ILimiterConfigService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>限流器配置接口实现</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-30 22:27
 **/
public class LimiterConfigServiceImpl implements ILimiterConfigService {

    private Map<String, LimiterConfig> limiters = new ConcurrentHashMap<>();

    @Override
    public void add(LimiterConfig limiterConfig) {
        limiters.put(limiterConfig.getKey(), limiterConfig);
    }

    @Override
    public LimiterConfig get(String limiterKey) {
        return limiters.get(limiterKey);
    }

    @Override
    public LimiterConfig convertLimiter(Limiter limiter) {
        LimiterConfig.Builder builder = LimiterConfig.newBuilder();
        builder.setName(limiter.name());
        builder.setKey(limiter.key());
        builder.setQps(limiter.qps());
        builder.setPermits(limiter.permits());
        builder.setTimeout(limiter.timeout());
        builder.setUnit(limiter.unit());
        return builder.build();
    }
}
