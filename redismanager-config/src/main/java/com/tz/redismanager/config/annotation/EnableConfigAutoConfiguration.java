package com.tz.redismanager.config.annotation;

import com.tz.redismanager.config.config.ConfigConfigurationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用配置管理自动配置的注解</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
//@EnableConfigurationProperties({ConfigProperties.class})
@Import(ConfigConfigurationSelector.class)
public @interface EnableConfigAutoConfiguration {

}
