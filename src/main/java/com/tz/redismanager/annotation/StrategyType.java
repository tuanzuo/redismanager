package com.tz.redismanager.annotation;


import com.tz.redismanager.enm.StrategyTypeEnum;

import java.lang.annotation.*;

/**
 * 处理策略类型注解
 *
 * @Since:2019-08-23 22:23:01
 * @Version:1.1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface StrategyType {

    StrategyTypeEnum[] value();

}
