package com.tz.redismanager.cacher.annotation;

import com.tz.redismanager.cacher.domain.InvalidateType;
import com.tz.redismanager.cacher.domain.InvocationStrategy;

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
public @interface CacheEvict {

    /**
     * 缓存名称
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
     * 缓存失效类型，默认失效所有
     */
    InvalidateType invalidate() default InvalidateType.ALL;

    /**
     * 在方法执行前或者执行后清理缓存，默认方法执行后清理缓存
     */
    InvocationStrategy invocation() default InvocationStrategy.AFTER;

}
