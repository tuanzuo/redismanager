package com.tz.redismanager.security;

import com.tz.redismanager.constant.ConstInterface;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用Auth注解</p>
 *
 * @version 1.5.0
 * @time 2020-11-05 23:55
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuthConfigurationSelector.class)
public @interface EnableAuth {

    //auth类型
    String authType() default ConstInterface.AuthType.REDIS;

}
