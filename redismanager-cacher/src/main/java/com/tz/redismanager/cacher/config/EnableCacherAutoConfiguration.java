package com.tz.redismanager.cacher.config;

import com.tz.redismanager.cacher.constant.ConstInterface;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用限流器自动配置的注解</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 23:05
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacherConfigurationSelector.class)
public @interface EnableCacherAutoConfiguration {

    String cacherType() default ConstInterface.CacherType.CACHER_DEFAULT;

}
