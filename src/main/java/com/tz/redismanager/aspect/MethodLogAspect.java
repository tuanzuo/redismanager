package com.tz.redismanager.aspect;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 输出方法入参和出参切面
 *
 * @Since:2019-08-23 22:23:26
 * @Version:1.1.0
 */
@Aspect
@Component
@Order(100)
public class MethodLogAspect {

    private static final Logger logger = TraceLoggerFactory.getLogger(MethodLogAspect.class);

    @Around("@annotation(methodLog)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            StringBuilder logInfo = new StringBuilder();
            if (StringUtils.isNotBlank(methodLog.logPrefix())) {
                logInfo.append("[").append(methodLog.logPrefix()).append("] ");
            }
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
                String path = servletRequestAttributes.getRequest().getServletPath();
                logInfo.append("[path-").append(path).append("] ");
                String sid = requestAttributes.getSessionId();
                logInfo.append("[sid-").append(sid).append("] ");
            }
            MethodSignature methodSignature = (MethodSignature) signature;
            String methodName = new StringBuilder(methodSignature.getDeclaringType().getSimpleName())
                    .append(ConstInterface.Symbol.SPOT).append(methodSignature.getMethod().getName()).toString();
            String logPrefix = logInfo.toString();
            if (methodLog.logInputParams()) {
                //得到参数
                Object[] args = joinPoint.getArgs();
                logger.info(logPrefix + "[{}] {入参:{}}", methodName, JsonUtils.toJsonStr(args));
            }
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            if (methodLog.logOutputParams() && methodLog.logExecTime()) {
                logger.info(logPrefix + "[{}] {出参:{},耗时:{}ms}", methodName, JsonUtils.toJsonStr(result), System.currentTimeMillis() - start);
            } else if (methodLog.logOutputParams()) {
                logger.info(logPrefix + "[{}] {出参:{}}", methodName, JsonUtils.toJsonStr(result));
            } else if (methodLog.logExecTime()) {
                logger.info(logPrefix + "[{}] {执行耗时:{}ms}", methodName, System.currentTimeMillis() - start);
            }
            return result;
        }
        return joinPoint.proceed();
    }
}
