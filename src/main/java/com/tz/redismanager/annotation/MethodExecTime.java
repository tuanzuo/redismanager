package com.tz.redismanager.annotation;


import java.lang.annotation.*;

/**
 * 方法执行时间注解
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodExecTime {

}
