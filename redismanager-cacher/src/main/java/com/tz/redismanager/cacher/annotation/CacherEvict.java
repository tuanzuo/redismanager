package com.tz.redismanager.cacher.annotation;

import java.lang.annotation.*;

/**
 * <p>缓存失效注解</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-06 0:39
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacherEvict {

    /**
     * 缓存器名称
     *
     * @return
     */
    String name();

    /**
     * 缓存器key
     */
    String key();

    /**
     * 缓存器key的变量,支持Spring Expression Language (SpEL)
     */
    String var();

    /**
     * 一级缓存
     */
    L1Cache l1Cache() default @L1Cache;

    /**
     * 二级缓存
     */
    L2Cache l2Cache() default @L2Cache;

}