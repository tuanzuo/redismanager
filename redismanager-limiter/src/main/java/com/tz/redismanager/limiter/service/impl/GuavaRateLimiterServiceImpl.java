package com.tz.redismanager.limiter.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.tz.redismanager.limiter.constant.ConstInterface;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.service.ILimiterService;
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
public class GuavaRateLimiterServiceImpl implements ILimiterService {

    private static ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    @Override
    public boolean support(String limiterType) {
        return ConstInterface.LimiterType.GUAVA_RATE_LIMITER.equals(limiterType);
    }

    @Override
    public boolean tryAcquire(Limiter limiter) {
        if (limiter.permits() <= 0) {
            return true;
        }
        return this.getLimiter(limiter).tryAcquire(limiter.permits(), limiter.timeout(), limiter.unit());
    }

    @Override
    public void initLimiter(Limiter limiter) {
        this.initRateLimiter(limiter);
    }

    private RateLimiter getLimiter(Limiter limiter) {
        String key = limiter.key();
        if (limiterMap.containsKey(key)) {
            return limiterMap.get(key);
        }
        return this.initRateLimiter(limiter);
    }

    private RateLimiter initRateLimiter(Limiter limiter) {
        RateLimiter rateLimiter = RateLimiter.create(limiter.qps());
        return Optional.ofNullable(limiterMap.putIfAbsent(limiter.key(), rateLimiter)).orElse(rateLimiter);
    }
}
