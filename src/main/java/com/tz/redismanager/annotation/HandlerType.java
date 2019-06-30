package com.tz.redismanager.annotation;


import com.tz.redismanager.enm.HandlerTypeEnum;

import java.lang.annotation.*;

/**
 * 处理器类型注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HandlerType {

    HandlerTypeEnum[] value();

}
