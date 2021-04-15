package com.tz.redismanager.config.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:22
 **/
@Configuration
public class AsyncEventConfig {

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10,
            500L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new CustomizableThreadFactory("AsyncEvent-thread-"),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(threadPoolExecutor);
        return eventMulticaster;
    }

}
