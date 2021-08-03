package com.tz.redismanager.config.annotation;

import com.tz.redismanager.config.config.ConfigConfigurationSelector;
import com.tz.redismanager.config.config.ConfigProperties;
import com.tz.redismanager.config.constant.ConstInterface;
import com.tz.redismanager.config.notify.zookeeper.ZookeeperProperties;
import com.tz.redismanager.config.notify.zookeeper.curator.CuratorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-25 22:21
 * @see org.springframework.context.annotation.EnableMBeanExport
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableConfigurationProperties({ConfigProperties.class, ZookeeperProperties.class, CuratorProperties.class})
@Import(ConfigConfigurationSelector.class)
public @interface EnableConfigAutoConfiguration {

    String configSyncType() default ConstInterface.ConfigSyncType.ZOOKEEPER_CURATOR;

}
