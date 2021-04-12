package com.tz.redismanager.limiter.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.tz.redismanager.limiter.config.LimiterConfig;
import com.tz.redismanager.limiter.constant.ConstInterface;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Guava RateLimiter限流器实现</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-21 22:12
 **/
@Service
@ConditionalOnClass(RateLimiter.class)
public class GuavaRateLimiterServiceImpl implements ILimiterService {

    private static ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    @Override
    public boolean support(String limiterType) {
        return ConstInterface.LimiterType.GUAVA_RATE_LIMITER.equals(limiterType);
    }

    @Override
    public boolean tryAcquire(LimiterConfig limiterConfig) {
        if (limiterConfig.getPermits() <= 0) {
            return true;
        }
        return this.getLimiter(limiterConfig).tryAcquire(limiterConfig.getPermits(), limiterConfig.getTimeout(), limiterConfig.getUnit());
    }

    @Override
    public void initLimiter(LimiterConfig limiterConfig) {
        this.initRateLimiter(limiterConfig);
    }

    @Override
    public void resetLimiter(LimiterConfig limiterConfig) {
        /**不能直接将老的限流器设置为空，因为老的限流器可能正在被其他请求使用*/
        /*RateLimiter oldRateLimiter = limiterMap.get(limiterConfig.getKey());
        if (null != oldRateLimiter) {
            oldRateLimiter = null;
        }*/
        RateLimiter rateLimiter = this.createRateLimiter(limiterConfig);
        limiterMap.put(limiterConfig.getKey(), rateLimiter);
    }

    private RateLimiter getLimiter(LimiterConfig limiterConfig) {
        String key = limiterConfig.getKey();
        if (limiterMap.containsKey(key)) {
            return limiterMap.get(key);
        }
        return this.initRateLimiter(limiterConfig);
    }

    private RateLimiter initRateLimiter(LimiterConfig limiterConfig) {
        RateLimiter rateLimiter = this.createRateLimiter(limiterConfig);
        return Optional.ofNullable(limiterMap.putIfAbsent(limiterConfig.getKey(), rateLimiter)).orElse(rateLimiter);
    }

    private RateLimiter createRateLimiter(LimiterConfig limiterConfig) {
        return RateLimiter.create(limiterConfig.getQps());
    }
}
