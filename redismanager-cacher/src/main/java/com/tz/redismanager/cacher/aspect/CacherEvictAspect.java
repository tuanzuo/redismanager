package com.tz.redismanager.cacher.aspect;

import com.tz.redismanager.cacher.annotation.CacherEvict;
import com.tz.redismanager.cacher.service.ICacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * <p>缓存器失效切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Order(100)
public class CacherEvictAspect extends AbstractAspect {

    private ICacheService cacheService;

    public CacherEvictAspect(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(cacherEvict)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, CacherEvict cacherEvict) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        String cacheKey = this.resolveCacheKey(joinPoint, cacherEvict.key(), cacherEvict.var());
        cacheService.invalidateCache(cacherEvict, cacheKey);
        return joinPoint.proceed();
    }

}
