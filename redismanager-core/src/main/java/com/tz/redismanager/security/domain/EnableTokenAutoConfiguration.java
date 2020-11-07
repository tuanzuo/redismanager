package com.tz.redismanager.security.domain;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.security.token.config.TokenConfigurationSelector;
import com.tz.redismanager.security.token.config.TokenProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用Token自动配置的注解</p>
 *
 * @version 1.5.0
 * @time 2020-11-05 23:55
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties({TokenProperties.class})
@Import(TokenConfigurationSelector.class)
public @interface EnableTokenAutoConfiguration {

    //token类型
    String tokenType() default ConstInterface.TokenType.REDIS;

}
