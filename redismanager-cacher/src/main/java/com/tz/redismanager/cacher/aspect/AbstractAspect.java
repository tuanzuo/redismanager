package com.tz.redismanager.cacher.aspect;

import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * <p>抽象的切面</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-06 0:51
 **/
public abstract class AbstractAspect {

    /**
     * 解析key
     * @param joinPoint
     * @param key 缓存key
     * @param var 缓存变量
     * @return
     */
    public String resolveCacheKey(ProceedingJoinPoint joinPoint, String key, String var) {
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
        return key + ":" + parser.parseExpression(var).getValue(context, String.class);
    }
}
