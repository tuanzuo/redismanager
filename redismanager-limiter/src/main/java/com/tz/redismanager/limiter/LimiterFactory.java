package com.tz.redismanager.limiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>限流器Factory</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:05
 **/
public class LimiterFactory {

    private static ConcurrentHashMap<String, RateLimiter> limiterMap = new ConcurrentHashMap<>();

    public static RateLimiter getLimiter(String key, double qps) {
        RateLimiter rateLimiter;
        if (limiterMap.containsKey(key)) {
            rateLimiter = limiterMap.get(key);
        } else {
            rateLimiter = RateLimiter.create(qps);
            rateLimiter = Optional.ofNullable(limiterMap.putIfAbsent(key, rateLimiter)).orElse(rateLimiter);
        }
        return rateLimiter;
    }

}
