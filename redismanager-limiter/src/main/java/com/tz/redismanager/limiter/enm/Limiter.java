package com.tz.redismanager.limiter.enm;


import java.lang.annotation.*;

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

}
