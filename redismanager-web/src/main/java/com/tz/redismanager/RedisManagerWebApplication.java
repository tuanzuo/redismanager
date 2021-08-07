package com.tz.redismanager;

import com.tz.redismanager.cacher.annotation.EnableCacherAutoConfiguration;
import com.tz.redismanager.config.annotation.EnableConfigAutoConfiguration;
import com.tz.redismanager.config.sdk.annotation.EnableConfigSdkAutoConfiguration;
import com.tz.redismanager.limiter.annotation.EnableLimiterAutoConfiguration;
import com.tz.redismanager.token.annotation.EnableTokenAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.tz"})
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
