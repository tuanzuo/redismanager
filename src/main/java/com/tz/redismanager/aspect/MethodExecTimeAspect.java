package com.tz.redismanager.aspect;

import com.tz.redismanager.annotation.MethodExecTime;
import com.tz.redismanager.constant.ConstInterface;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 方法执行耗时切面
 *
 * @Title:
 * @Description:
 * @Author:Administrator
 * @Version:1.1.0
 */
@Aspect
@Component
@Order(100)
public class MethodExecTimeAspect {

    private static final Logger logger = LoggerFactory.getLogger(MethodExecTimeAspect.class);

    @Around("@annotation(methodExecTime)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, MethodExecTime methodExecTime) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            String methodName = new StringBuilder(joinPoint.getTarget().getClass().getSimpleName())
                    .append(ConstInterface.Symbol.SPOT).append(methodSignature.getMethod().getName()).toString();
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            logger.info("[MethodExecTimeAspect] [{}] {执行耗时:{}ms}", methodName, System.currentTimeMillis() - start);
            return result;
        }
        return joinPoint.proceed();
    }
}
