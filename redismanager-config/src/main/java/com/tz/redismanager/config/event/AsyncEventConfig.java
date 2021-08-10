package com.tz.redismanager.config.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>异步事件配置</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:22
 **/
@Configuration
public class AsyncEventConfig {

    @Value("${config.async.event.threadNamePrefix:ConfigAsyncEvent-thread-}")
    private String threadNamePrefix;
    @Value("${config.async.event.allowCoreThreadTimeOut:true}")
    private boolean allowCoreThreadTimeOut;
    @Value("${config.async.event.corePoolSize:5}")
    private int corePoolSize;
    @Value("${config.async.event.maxPoolSize:10}")
    private int maxPoolSize;
    @Value("${config.async.event.keepAliveTime:500}")
    private long keepAliveTime;

    @Bean(name = "applicationEventMulticaster")
    public ApplicationEventMulticaster simpleApplicationEventMulticaster() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                keepAliveTime, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new CustomizableThreadFactory(threadNamePrefix),
                new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.allowCoreThreadTimeOut(allowCoreThreadTimeOut);
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(threadPoolExecutor);
        return eventMulticaster;
    }

}
