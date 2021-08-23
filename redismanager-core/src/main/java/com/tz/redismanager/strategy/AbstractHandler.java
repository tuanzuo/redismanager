package com.tz.redismanager.strategy;

import com.alibaba.fastjson.TypeReference;
import com.tz.redismanager.constant.ConstInterface;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 抽象的处理器
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-23 21:35
 **/
public abstract class AbstractHandler<T, R> implements IHandler<T, R> {

    public final static Type SET_STRING_TYPE = new TypeReference<Set<String>>() {}.getType();
    public final static Type ZSET_STRING_TYPE = new TypeReference<Set<ZSetValue>>() {}.getType();
    public final static Type LIST_STRING_TYPE = new TypeReference<List<String>>() {}.getType();
    public final static Type HASH_STRING_TYPE = new TypeReference<Map<String,String>>() {}.getType();

    public void setValueAndExpireTimeForStringType(String key, Object value, RedisTemplate<String, Object> redisTemplate, Long expireTime) {
        if (ConstInterface.Common.NO_EXPIRE == expireTime) {
            redisTemplate.opsForValue().set(key, value);
        } else if (expireTime > Integer.MAX_VALUE) {
            redisTemplate.opsForValue().set(key, value);
            this.setKeyExpireTime(redisTemplate, key, expireTime);
        } else if (expireTime > ConstInterface.Common.ZERO) {
            redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
        }
    }

    public void setKeyExpireTime(RedisTemplate<String, Object> redisTemplate, String key, Long expireTime) {
        if (null != expireTime && expireTime > ConstInterface.Common.ZERO) {
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ZSetValue zSetValue = (ZSetValue) o;
            return Objects.equals(score, zSetValue.score) &&
                    Objects.equals(value, zSetValue.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, value);
        }
    }
}
