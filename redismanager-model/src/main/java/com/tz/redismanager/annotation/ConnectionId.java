package com.tz.redismanager.annotation;

import java.lang.annotation.*;

/**
 * 标识redis连接的id注解
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-19 23:11
 **/
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ConnectionId {
}
