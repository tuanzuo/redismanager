package com.tz.redismanager.annotation;


import java.lang.annotation.*;

/**
 * 输出方法入参和出参日志注解
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:21:34
 * @Version:1.1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MethodLog {

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
