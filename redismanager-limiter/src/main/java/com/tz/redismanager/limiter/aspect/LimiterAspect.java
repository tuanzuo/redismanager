package com.tz.redismanager.limiter.aspect;

import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.exception.LimiterException;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * <p>限流器切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Order(100)
public class LimiterAspect {

    private ILimiterService limiterService;

    public LimiterAspect(ILimiterService limiterService) {
        this.limiterService = limiterService;
    }

    @Around("@annotation(limiter)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, Limiter limiter) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        if (limiterService.tryAcquire(limiter)) {
            return joinPoint.proceed();
        }
        throw new LimiterException(ResultCode.LIMIT_EXCEPTION.getCode(), limiter.name() + "-" + ResultCode.LIMIT_EXCEPTION.getMsg());
    }

}
