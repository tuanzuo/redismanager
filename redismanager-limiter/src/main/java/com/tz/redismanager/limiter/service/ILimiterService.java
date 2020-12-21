package com.tz.redismanager.limiter.service;

import com.tz.redismanager.limiter.enm.Limiter;

/**
 * <p>限流器接口</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-21 22:11
 **/
public interface ILimiterService {

    boolean support(String tokenType);

    boolean tryAcquire(Limiter limiter);

}
