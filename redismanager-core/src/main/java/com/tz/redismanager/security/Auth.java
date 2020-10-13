package com.tz.redismanager.security;


import java.lang.annotation.*;

/**
 * <p>Token认证注解</p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Auth {

    /**
     * 是否必须
     *
     * @return
     */
    boolean required() default true;

    /**
     * 允许的角色列表
     *
     * @return
     */
    String[] permitRoles() default {};

}
