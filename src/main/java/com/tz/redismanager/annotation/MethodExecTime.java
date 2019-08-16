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

    /**
     * 日志前缀
     */
    String logPrefix() default "";

    /**
     * 是否打印入参：true是,false否
     */
    boolean logInputParams() default true;

    /**
     * 是否打印出参：true是,false否
     */
    boolean logOutputParams() default true;

    /**
     * 是否打印方法执行时间：true是,false否
     */
    boolean logExecTime() default true;

}
