package com.tz.redismanager.cacher.aspect;

import com.tz.redismanager.cacher.annotation.CacheEvict;
import com.tz.redismanager.cacher.config.CacheEvictConfig;
import com.tz.redismanager.cacher.domain.InvocationStrategy;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.service.ICacherConfigService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * <p>缓存失效切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Order(200) //https://blog.csdn.net/zzhongcy/article/details/109504563
public class CacheEvictAspect extends AbstractAspect {

    private ICacheService cacheService;
    private ICacherConfigService cacherConfigService;

    public CacheEvictAspect(ICacheService cacheService, ICacherConfigService cacherConfigService) {
        this.cacheService = cacheService;
        this.cacherConfigService = cacherConfigService;
    }

    @Around("@annotation(cacheEvict)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, CacheEvict cacheEvict) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        String cacheKey = this.resolveCacheKey(joinPoint, cacheEvict.key(), cacheEvict.var());
        CacheEvictConfig cacheEvictConfig = cacherConfigService.convertCacheEvict(cacheEvict);
        InvocationStrategy invocationStrategy = cacheEvict.invocation();
        if (InvocationStrategy.BEFORE == invocationStrategy) {
            cacheService.invalidateCache(cacheEvictConfig, cacheKey);
        }
        Object result = joinPoint.proceed();
        if (InvocationStrategy.AFTER == invocationStrategy) {
            cacheService.invalidateCache(cacheEvictConfig, cacheKey);
        }
        return result;
    }

}
