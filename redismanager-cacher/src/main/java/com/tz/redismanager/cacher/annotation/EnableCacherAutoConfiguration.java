package com.tz.redismanager.cacher.annotation;

import com.tz.redismanager.cacher.config.CacherConfigurationSelector;
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

    /**
     * 缓存器实现类型
     * @return
     */
    String cacherType() default ConstInterface.CacherType.DEFAULT_CACHER;

    /**
     * 是否在应用启动的时候初始化缓存器，默认是(true)
     * @return
     */
    boolean initCacher() default true;

    /**
     * 初始化缓存器扫描的路径
     * @return
     */
    String initCacherScanPackage() default "com.tz";

}
