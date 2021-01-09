package com.tz.redismanager.limiter.annotation;


import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>限流器注解</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:08
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limiter {

    /**
     * 限流器名称
     *
     * @return
     */
    String name();

    /**
     * 限流器key
     */
    String key();

    /**
     * qps
     */
    double qps();

    /**
     * 获得许可的数量，默认为1
     */
    int permits() default 1;

    /**
     * 获得许可的超时时间，默认为0(不超时)
     */
    long timeout() default 0;

    /**
     * 获得许可的超时时间的单位，默认为TimeUnit.MICROSECONDS
     */
    TimeUnit unit() default TimeUnit.MICROSECONDS;


}
