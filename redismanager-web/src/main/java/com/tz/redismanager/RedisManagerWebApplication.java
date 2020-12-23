package com.tz.redismanager;

import com.tz.redismanager.limiter.config.EnableLimiterAutoConfiguration;
import com.tz.redismanager.security.domain.EnableTokenAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.tz"})
@PropertySource("${rm.config}")
@EnableTokenAutoConfiguration()
@EnableLimiterAutoConfiguration()
public class RedisManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisManagerWebApplication.class, args);
    }

}
