package com.tz.redismanager.annotation;


import com.tz.redismanager.enm.HandlerTypeEnum;

import java.lang.annotation.*;

/**
 * 处理器类型注解
 *
 * @Since:2019-08-23 22:20:46
 * @Version:1.1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HandlerType {

    HandlerTypeEnum[] value();

}
