package com.tz.redismanager.cacher.annotation;

import com.tz.redismanager.cacher.domain.ExpireStrategy;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>一级缓存注解</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-25 21:51
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface L1Cache {

    boolean enable() default true;

    int initialCapacity() default 100;

    long maximumSize() default 1000;

    ExpireStrategy expireStrategy() default ExpireStrategy.EXPIRE_AFTER_WRITE;

    long expireDuration() default 2;

    TimeUnit expireUnit() default TimeUnit.MINUTES;

    boolean recordStats() default true;

}
