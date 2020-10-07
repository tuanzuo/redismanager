package com.tz.redismanager.strategy;

import com.alibaba.fastjson.TypeReference;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的处理器
 *
 * @version 1.0
 * @time 2019-06-23 21:35
 **/
public abstract class AbstractHandler<T, R> implements IHandler<T, R> {

    public final static Type SET_STRING_TYPE = new TypeReference<Set<String>>() {}.getType();
    public final static Type ZSET_STRING_TYPE = new TypeReference<Set<ZSetValue>>() {}.getType();
    public final static Type LIST_STRING_TYPE = new TypeReference<List<String>>() {}.getType();
    public final static Type HASH_STRING_TYPE = new TypeReference<Map<String,String>>() {}.getType();

    public void setValueForStringType(String key, Object value, RedisTemplate<String, Object> redisTemplate, Long expireTime) {
        if (null != expireTime && null != key && null != value) {
            if (-1 == expireTime) {
                redisTemplate.opsForValue().set(key, value);
            } else if (expireTime > Integer.MAX_VALUE) {
                redisTemplate.opsForValue().set(key, value);
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            } else if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            }
        }
    }

    public static class ZSetValue{
        private Double score;
        private String value;

        public ZSetValue() {
        }

        public ZSetValue(Double score, String value) {
            this.score = score;
            this.value = value;
        }

        public Double getScore() {
            return score;
        }

        public void setScore(Double score) {
            this.score = score;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
