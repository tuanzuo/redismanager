package com.tz.redismanager.limiter.aspect;

import com.google.common.util.concurrent.RateLimiter;
import com.tz.redismanager.limiter.LimiterFactory;
import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.enm.Limiter;
import com.tz.redismanager.limiter.exception.LimiterException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * <p>限流器切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Component
@Order(100)
public class LimiterAspect {

    @Around("@annotation(limiter)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, Limiter limiter) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        RateLimiter rateLimiter = LimiterFactory.getLimiter(limiter.key(), limiter.qps());
        if (rateLimiter.tryAcquire(10, TimeUnit.MICROSECONDS)) {
            return joinPoint.proceed();
        }
        throw new LimiterException(ResultCode.LIMIT_EXCEPTION.getCode(), limiter.name() + "-" + ResultCode.LIMIT_EXCEPTION.getMsg());
    }

}
