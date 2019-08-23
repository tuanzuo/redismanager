package com.tz.redismanager.aspect;

import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 设置RedisTemplate切面
 *
 * @Since:2019-08-23 22:23:40
 * @Version:1.1.0
 */
@Aspect
@Component
@Order(200)
public class SetRedisTemplateAspect {

    private static final Logger logger = LoggerFactory.getLogger(SetRedisTemplateAspect.class);

    @Autowired
    private IRedisContextService redisContextService;

    @Around("@annotation(setRedisTemplate)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, SetRedisTemplate setRedisTemplate) throws Throwable {
        Signature signature = joinPoint.getSignature();
        if (signature instanceof MethodSignature) {
            Integer index = null;
            MethodSignature methodSignature = (MethodSignature) signature;
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            if (ArrayUtils.isNotEmpty(annotations)) {
                int i = 0;
                label:
                for (Annotation[] array : annotations) {
                    if (ArrayUtils.isNotEmpty(array)) {
                        for (Annotation annotation : array) {
                            if (null != annotation && annotation.annotationType().getName().equals(ConnectionId.class.getName())) {
                                index = i;
                                break label;
                            }
                        }
                    }
                    i++;
                }
            }

            String id = null;
            //得到参数
            Object[] args = joinPoint.getArgs();
            if (null != index && ArrayUtils.isNotEmpty(args) && null != args[index]) {
                id = args[index].toString();
            } else if (ArrayUtils.isNotEmpty(args)) {
                label:
                for (Object arg : args) {
                    if (null != arg) {
                        Field[] fields = arg.getClass().getDeclaredFields();
                        if (ArrayUtils.isNotEmpty(fields)) {
                            for (Field field : fields) {
                                if (null != field && null != field.getDeclaredAnnotation(ConnectionId.class)) {
                                    ReflectionUtils.makeAccessible(field);
                                    Object obj = field.get(arg);
                                    if (null != obj) {
                                        id = obj.toString();
                                        break label;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotBlank(id)) {
                RedisTemplate<String, Object> redisTemplate = redisContextService.getRedisTemplate(id);
                if (null == redisTemplate) {
                    //初始化redisTemplate
                    redisTemplate = redisContextService.initContext(id);
                }
                if (null == redisTemplate) {
                    logger.error("[SetRedisTemplateAspect] [annotationPointCut] {id:{}查询不到redisTemplate}", id);
                    if (setRedisTemplate.whenIsNullContinueExec()) {
                        return joinPoint.proceed();
                    } else {
                        throw new RmException(ResultCode.REDIS_TEMPLATE_ISNULL.getCode(), "RedisTemplate为空,操作失败!");
                    }
                }
                RedisContextUtils.setRedisTemplate(redisTemplate);
                //执行方法
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
        }
        return joinPoint.proceed();
    }
}
