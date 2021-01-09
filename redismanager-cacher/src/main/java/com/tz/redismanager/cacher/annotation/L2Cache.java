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

    boolean enable() default true;

    long expireDuration() default 10;

    TimeUnit expireUnit() default TimeUnit.MINUTES;

}
