package com.tz.redismanager.aspect;

import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Optional;

/**
 * 设置RedisTemplate切面
 *
 * @Since:2019-08-23 22:23:40
 * @Version:1.1.0
 */
@Aspect
@Component
@Order(150)
public class SetRedisTemplateAspect {

    private static final Logger logger = TraceLoggerFactory.getLogger(SetRedisTemplateAspect.class);

    @Autowired
    private IRedisContextService redisContextService;

    @Around("@annotation(setRedisTemplate)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, SetRedisTemplate setRedisTemplate) throws Throwable {
        Signature signature = joinPoint.getSignature();
        boolean methodSignFlag = signature instanceof MethodSignature;
        if (!methodSignFlag) {
            return joinPoint.proceed();
        }
        Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isEmpty(args)) {
            return joinPoint.proceed();
        }

        String id = this.getIdFromParam(signature, args);
        id = Optional.ofNullable(id).orElse(this.getIdFromParamObj(id, args));
        if (StringUtils.isBlank(id)) {
            return joinPoint.proceed();
        }

        RedisTemplate<String, Object> redisTemplate = redisContextService.getRedisTemplate(id);
        redisTemplate = Optional.ofNullable(redisTemplate).orElse(redisContextService.initContext(id));
        if (null == redisTemplate) {
            logger.warn("通过id:{}查询不到redisTemplate", id);
            if (setRedisTemplate.whenIsNullContinueExec()) {
                return joinPoint.proceed();
            } else {
                throw new RmException(ResultCode.REDIS_TEMPLATE_ISNULL.getCode(), "RedisTemplate为空,操作失败!");
            }
        }
        RedisContextUtils.setRedisTemplate(redisTemplate);
        Object result;
        //得到设置的序列化对象
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        RedisSerializer hashKeySerializer = redisTemplate.getHashKeySerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        RedisSerializer hashValueSerializer = redisTemplate.getHashValueSerializer();
        try {
            result = joinPoint.proceed();
        } finally {
            //还原设置的序列化对象
            redisTemplate.setKeySerializer(keySerializer);
            redisTemplate.setHashKeySerializer(hashKeySerializer);
            redisTemplate.setValueSerializer(valueSerializer);
            redisTemplate.setHashValueSerializer(hashValueSerializer);
            RedisContextUtils.cleanRedisTemplate();
        }
        return result;
    }

    /**
     * 从参数中得到被{@link ConnectionId}注解标识的参数的值
     */
    private String getIdFromParam(Signature signature, Object[] args) {
        /**找到{@link ConnectionId}注解标识的参数的位置*/
        Integer index = this.findConnectionIdToMarkParamIndex(signature);
        String id = null;
        if (null != index && null != args[index]) {
            id = args[index].toString();
        }
        return id;
    }

    /**
     * 从参数对象中得到被{@link ConnectionId}注解标识的属性的值
     */
    private String getIdFromParamObj(String id, Object[] args) throws IllegalAccessException {
        label:
        for (Object arg : args) {
            if (null == arg) {
                continue;
            }
            Field[] fields = arg.getClass().getDeclaredFields();
            if (ArrayUtils.isEmpty(fields)) {
                continue;
            }
            for (Field field : fields) {
                if (null == field || null == field.getDeclaredAnnotation(ConnectionId.class)) {
                    continue;
                }
                ReflectionUtils.makeAccessible(field);
                Object obj = field.get(arg);
                if (null == obj) {
                    continue;
                }
                id = obj.toString();
                break label;
            }
        }
        return id;
    }

    private Integer findConnectionIdToMarkParamIndex(Signature signature) {
        MethodSignature methodSignature = (MethodSignature) signature;
        Integer index = null;
        Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
        if (ArrayUtils.isEmpty(annotations)) {
            return index;
        }
        int i = -1;
        label:
        for (Annotation[] array : annotations) {
            i++;
            if (ArrayUtils.isEmpty(array)) {
                continue;
            }
            for (Annotation annotation : array) {
                if (null != annotation && annotation.annotationType().getName().equals(ConnectionId.class.getName())) {
                    index = i;
                    break label;
                }
            }
        }
        return index;
    }
}
