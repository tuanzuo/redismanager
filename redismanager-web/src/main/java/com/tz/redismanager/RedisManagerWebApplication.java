package com.tz.redismanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages = {"com.tz"})
@PropertySource("${redis.config}")
public class RedisManagerWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisManagerWebApplication.class, args);
    }

}
