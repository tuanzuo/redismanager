package com.tz.redismanager;

import com.tz.redismanager.cacher.config.EnableCacherAutoConfiguration;
import com.tz.redismanager.limiter.config.EnableLimiterAutoConfiguration;
import com.tz.redismanager.token.config.EnableTokenAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.tz"})
@PropertySource("${rm.config}")
@EnableTokenAutoConfiguration()
@EnableLimiterAutoConfiguration()
@EnableCacherAutoConfiguration()
public class RedisManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisManagerWebApplication.class, args);
    }

}
