package com.tz.redismanager.annotation;

import java.lang.annotation.*;

/**
 * 日志msg注解
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-08-30 20:19
 **/
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LoggerMsg {
}
