package com.tz.redismanager.token.annotation;

import com.tz.redismanager.token.config.TokenConfigurationSelector;
import com.tz.redismanager.token.config.TokenProperties;
import com.tz.redismanager.token.constant.ConstInterface;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>启用Token自动配置的注解</p>
 *
 * @author tuanzuo
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
    String tokenType() default ConstInterface.TokenType.JWT;

}
