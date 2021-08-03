package com.tz.redismanager.annotation;

import java.lang.annotation.*;

/**
 * 设置RedisTemplate注解

 * @author tuanzuo
 * @Since:2019-08-23 22:21:56
 * @Version:1.1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SetRedisTemplate {

    /**
     * 当RedisTemplate为空时是否继续执行目标方法：true继续执行,false不继续执行,默认false
     */
    boolean whenIsNullContinueExec() default false;
}
