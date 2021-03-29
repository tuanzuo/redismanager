package com.tz.redismanager.cacher.aspect;

import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.config.CacherConfig;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.service.ICacherConfigService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * <p>缓存生效切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
//@Order(200) //https://blog.csdn.net/zzhongcy/article/details/109504563
public class CacheableAspect extends AbstractAspect implements Ordered {

    private static final Logger logger = LoggerFactory.getLogger(CacheableAspect.class);

    private ICacheService cacheService;
    private ICacherConfigService cacherConfigService;
    private ICacheableAspectConfigCustomizer configCustomizer;

    public CacheableAspect(ICacheService cacheService, ICacherConfigService cacherConfigService, ICacheableAspectConfigCustomizer configCustomizer) {
        this.cacheService = cacheService;
        this.cacherConfigService = cacherConfigService;
        this.configCustomizer = configCustomizer;
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
        CacherConfig cacherConfig = Optional.ofNullable(cacherConfigService.get(cacheable.key())).orElse(cacherConfigService.convertCacheable(cacheable));
        return cacheService.getCache(cacherConfig, cacheKey, genericReturnType, (temp) -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                logger.error("[缓存器] [{}] [{}] [回源查询异常] [{}]", cacheable.key(), cacheable.name(), cacheKey, throwable);
                throw new CacherException(ResultCode.GET_ORI_RESULT_EXCEPTION, throwable);
            }
        });
    }

    @Override
    public int getOrder() {
        /**通过实现自定义切面配置服务来修改切面的order*/
        return null != configCustomizer ? configCustomizer.customizeOrder() : 200;
    }
}
