package com.tz.redismanager.util;

import com.tz.redismanager.trace.TraceLoggerFactory;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 公共工具类
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:52:15
 * @Version:1.1.0
 */
public class CommonUtils {

    private static final Logger logger = TraceLoggerFactory.getLogger(CommonUtils.class);

    public static String getExcpMsg(Throwable e) {
        Throwable old = e;
        Throwable cause = e.getCause();
        while (null != cause) {
            old = cause;
            cause = cause.getCause();
        }
        return new StringBuilder(old.getClass().getName()).append("：").append(old.getMessage()).toString();
    }

    /**
     * 重新设置keySerializer
     *
     * @param redisTemplate
     */
    public static void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        if (null != keySerializer) {
            if (keySerializer.getClass().getName().equals(StringRedisSerializer.class.getName())) {
                redisTemplate.setKeySerializer(redisTemplate.getDefaultSerializer());
            } else {
                redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
            }
            logger.info("[CommonUtils] [reSetKeySerializer] {keySerializer从{}切换为:{}再查询}", keySerializer.getClass().getSimpleName(), redisTemplate.getKeySerializer().getClass().getSimpleName());
        }
    }

}
