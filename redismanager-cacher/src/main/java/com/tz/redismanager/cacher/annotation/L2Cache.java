package com.tz.redismanager.cacher.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>二级缓存注解</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-25 21:51
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface L2Cache {

    /**
     * 是否开启二级缓存，true:开启，false:关闭
     */
    boolean enable() default true;

    /**
     * 过期时间
     */
    long expireDuration() default 10;

    /**
     * 过期时间单位
     */
    TimeUnit expireUnit() default TimeUnit.MINUTES;

}
