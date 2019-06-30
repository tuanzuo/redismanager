package com.tz.redismanager.annotation;

import java.lang.annotation.*;

/**
 * 设置RedisTemplate注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetRedisTemplate {
}
