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

    /**
     * 是否开启一级缓存，true:开启，false:关闭
     */
    boolean enable() default true;

    /**
     * 初始的缓存空间大小
     */
    int initialCapacity() default 100;

    /**
     * 缓存的最大条数
     */
    long maximumSize() default 1000;

    /**
     * 过期策略
     */
    ExpireStrategy expireStrategy() default ExpireStrategy.EXPIRE_AFTER_WRITE;

    /**
     * 过期时间
     */
    long expireDuration() default 2;

    /**
     * 过期时间单位
     */
    TimeUnit expireUnit() default TimeUnit.MINUTES;

    /**
     * 开启数据统计功能，true:开启，false:关闭
     */
    boolean recordStats() default true;

}
