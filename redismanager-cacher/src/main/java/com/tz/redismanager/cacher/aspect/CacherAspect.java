package com.tz.redismanager.cacher.aspect;

import com.tz.redismanager.cacher.domain.Cacher;
import com.tz.redismanager.cacher.domain.ResultCode;
import com.tz.redismanager.cacher.exception.CacherException;
import com.tz.redismanager.cacher.service.ICacheService;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Type;

/**
 * <p>缓存器切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Aspect
@Order(100)
public class CacherAspect {

    private ICacheService cacheService;

    public CacherAspect(ICacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Around("@annotation(cacher)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, Cacher cacher) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Type genericReturnType = methodSignature.getMethod().getGenericReturnType();
        String cacheKey = this.resolveCacheKey(joinPoint, cacher);
        return cacheService.getCache(cacher, cacheKey, genericReturnType, (temp) -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                throw new CacherException(ResultCode.GET_ORI_RESULT_EXCEPTION, throwable);
            }
        });
    }

    private String resolveCacheKey(ProceedingJoinPoint joinPoint, Cacher cacher) {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        methodSignature.getMethod().getGenericReturnType();
        String[] names = methodSignature.getParameterNames();
        //表达式的上下文
        EvaluationContext context = new StandardEvaluationContext();
        if (ArrayUtils.isNotEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                //为了让表达式可以访问该对象, 先把对象放到上下文中
                context.setVariable(names[i], args[i]);
            }
        }
        ExpressionParser parser = new SpelExpressionParser();
        return cacher.key() + ":" + parser.parseExpression(cacher.var()).getValue(context, String.class);
    }

}
