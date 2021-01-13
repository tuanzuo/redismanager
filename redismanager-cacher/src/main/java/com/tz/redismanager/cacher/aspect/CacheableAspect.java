package com.tz.redismanager.cacher.aspect;

import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Type;

/**
 * <p>缓存生效切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Order(200)
public class CacheableAspect extends AbstractAspect {

    private ICacheService cacheService;

    public CacheableAspect(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(cacheable)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, Cacheable cacheable) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Type genericReturnType = methodSignature.getMethod().getGenericReturnType();
        String cacheKey = this.resolveCacheKey(joinPoint, cacheable.key(), cacheable.var());
        return cacheService.getCache(cacheable, cacheKey, genericReturnType, (temp) -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CacherException(ResultCode.GET_ORI_RESULT_EXCEPTION, throwable);
            }
        });
    }

}
