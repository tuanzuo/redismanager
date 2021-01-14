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
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 方法入参和出参日志切面
 *
 * @Since:2019-08-23 22:23:26
 * @Version:1.1.0
 */
@Aspect
@Component
@Order(50) //https://blog.csdn.net/zzhongcy/article/details/109504563
public class MethodLogAspect {

    private static final Logger logger = TraceLoggerFactory.getLogger(MethodLogAspect.class);

    @Around("@annotation(methodLog)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, MethodLog methodLog) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        //日志前缀
        StringBuilder logBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(methodLog.logPrefix())) {
            logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_LEFT).append(methodLog.logPrefix()).append(ConstInterface.Symbol.MIDDLE_BRACKET_RIGHT).append(" ");
        }
        //获取请求path
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            String path = servletRequestAttributes.getRequest().getServletPath();
            logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_LEFT).append("path-").append(path).append(ConstInterface.Symbol.MIDDLE_BRACKET_RIGHT).append(" ");
        }
        //获取方法
        MethodSignature methodSignature = (MethodSignature) signature;
        String methodName = new StringBuilder(methodSignature.getDeclaringType().getSimpleName())
                .append(ConstInterface.Symbol.SPOT).append(methodSignature.getMethod().getName()).toString();
        logBuilder.append(ConstInterface.Symbol.MIDDLE_BRACKET_LEFT).append(methodName).append(ConstInterface.Symbol.MIDDLE_BRACKET_RIGHT);

        if (methodLog.logInputParams()) {
            logger.info(logBuilder.toString() + "{入参:{}}", JsonUtils.toJsonStr(joinPoint.getArgs()));
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        boolean flag = false;
        if (methodLog.logOutputParams()) {
            flag = true;
            logBuilder.append("{出参:").append(JsonUtils.toJsonStr(result)).append("}");
        }
        if (methodLog.logExecTime()) {
            flag = true;
            logBuilder.append("{执行耗时:").append(stopWatch.getTotalTimeMillis()).append("ms}");
        }
        if (flag) {
            logger.info(logBuilder.toString());
        }
        return result;
    }
}
