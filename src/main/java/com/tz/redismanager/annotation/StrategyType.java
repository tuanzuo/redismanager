package com.tz.redismanager.annotation;


import com.tz.redismanager.enm.StrategyTypeEnum;

import java.lang.annotation.*;

/**
 * 处理策略类型注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface StrategyType {

    StrategyTypeEnum[] value();

}
