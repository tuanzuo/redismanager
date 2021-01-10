package com.tz.redismanager.limiter.annotation;

import com.tz.redismanager.limiter.config.LimiterConfigurationSelector;
import com.tz.redismanager.limiter.constant.ConstInterface;
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
@Import(LimiterConfigurationSelector.class)
public @interface EnableLimiterAutoConfiguration {

    /**
     * 限流器类型，默认为Guava限流
     * @return
     */
    String limiterType() default ConstInterface.LimiterType.GUAVA_RATE_LIMITER;

    /**
     * 是否在应用启动的时候初始化限流器，默认是(true)
     * @return
     */
    boolean initLimiter() default true;

    /**
     * 初始化限流器扫描的路径
     * @return
     */
    String initLimiterScanPackage() default "com.tz";

}
