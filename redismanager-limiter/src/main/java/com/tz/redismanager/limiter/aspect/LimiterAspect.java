package com.tz.redismanager.limiter.aspect;

import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.limiter.config.LimiterConfig;
import com.tz.redismanager.limiter.domain.ResultCode;
import com.tz.redismanager.limiter.exception.LimiterException;
import com.tz.redismanager.limiter.service.ILimiterConfigService;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

/**
 * <p>限流器切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
//@Order(100) //https://blog.csdn.net/zzhongcy/article/details/109504563
public class LimiterAspect implements Ordered {

    private ILimiterService limiterService;
    private ILimiterConfigService limiterConfigService;
    private ILimiterAspectConfigCustomizer configCustomizer;

    public LimiterAspect(ILimiterService limiterService, ILimiterConfigService limiterConfigService, ILimiterAspectConfigCustomizer configCustomizer) {
        this.limiterService = limiterService;
        this.limiterConfigService = limiterConfigService;
        this.configCustomizer = configCustomizer;
    }

    @Around("@annotation(limiter)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, Limiter limiter) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        LimiterConfig limiterConfig = limiterConfigService.convertLimiter(limiter);
        if (limiterService.tryAcquire(limiterConfig)) {
            return joinPoint.proceed();
        }
        throw new LimiterException(ResultCode.LIMIT_EXCEPTION.getCode(), limiterConfig.getName() + "-" + ResultCode.LIMIT_EXCEPTION.getMsg());
    }

    @Override
    public int getOrder() {
        /**通过实现自定义切面配置服务来修改切面的order*/
        return null != configCustomizer ? configCustomizer.customizeOrder() : 100;
    }
}
