package com.tz.redismanager;

import com.tz.redismanager.cacher.annotation.EnableCacherAutoConfiguration;
import com.tz.redismanager.config.annotation.EnableConfigAutoConfiguration;
import com.tz.redismanager.config.sdk.annotation.EnableConfigSdkAutoConfiguration;
import com.tz.redismanager.limiter.annotation.EnableLimiterAutoConfiguration;
import com.tz.redismanager.token.annotation.EnableTokenAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * <p>web启动类</p>
 *
 * @author tuanzuo
 * @version 1.0.0
 * @time 2019-05-31 0:14
 **/
@SpringBootApplication(scanBasePackages = {"com.tz", "com.baidu.fsg.uid"})
@MapperScan({"com.tz.redismanager.dao.mapper", "com.baidu.fsg.uid.worker.dao"})
@PropertySource("${rm.config}")
@EnableTokenAutoConfiguration
@EnableLimiterAutoConfiguration
@EnableCacherAutoConfiguration
@EnableConfigAutoConfiguration
@EnableConfigSdkAutoConfiguration
public class RedisManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisManagerWebApplication.class, args);
    }

}
