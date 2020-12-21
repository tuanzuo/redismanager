package com.tz.redismanager.limiter.service.impl;

import com.google.common.util.concurrent.RateLimiter;
import com.tz.redismanager.limiter.constant.ConstInterface;
import com.tz.redismanager.limiter.enm.Limiter;
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
public class RateLimiterServiceImpl implements ILimiterService {

    private static ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    @Override
    public boolean support(String tokenType) {
        return ConstInterface.LimiterType.GUAVA_RATE_LIMITER.equals(tokenType);
    }

    @Override
    public boolean tryAcquire(Limiter limiter) {
        return this.getLimiter(limiter).tryAcquire();
    }

    private RateLimiter getLimiter(Limiter limiter) {
        String key = limiter.key();
        RateLimiter rateLimiter;
        if (limiterMap.containsKey(key)) {
            rateLimiter = limiterMap.get(key);
        } else {
            rateLimiter = RateLimiter.create(limiter.qps());
            rateLimiter = Optional.ofNullable(limiterMap.putIfAbsent(key, rateLimiter)).orElse(rateLimiter);
        }
        return rateLimiter;
    }
}
