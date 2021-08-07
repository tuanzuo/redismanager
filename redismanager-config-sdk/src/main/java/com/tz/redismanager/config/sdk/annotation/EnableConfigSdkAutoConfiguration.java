package com.tz.redismanager.config.sdk.annotation;

import com.tz.redismanager.config.sdk.config.ConfigSdkConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用配置sdk自动配置的注解</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConfigSdkConfigurationSelector.class)
public @interface EnableConfigSdkAutoConfiguration {

}
