package com.tz.redismanager.aspect;

import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RedisContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SetRedisTemplateAspect {

    private static final Logger logger = LoggerFactory.getLogger(SetRedisTemplateAspect.class);

    @Autowired
    private IRedisContextService redisContextService;

    @Around("@annotation(setRedisTemplate)")
    public Object annotationPointCut(ProceedingJoinPoint joinPoint, SetRedisTemplate setRedisTemplate) throws Throwable {
        //得到参数
        Object[] args = joinPoint.getArgs();
        String id = args[0].toString();
        RedisTemplate<String, Object> redisTemplate = redisContextService.getRedisTemplate(id);
        if (null == redisTemplate) {
            //初始化redisTemplate
            redisTemplate = redisContextService.initContext(id);
        }
        if (null == redisTemplate) {
            logger.error("[SetRedisTemplateAspect] [annotationPointCut] {id:{}查询不到redisTemplate}", id);
            return null;
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
