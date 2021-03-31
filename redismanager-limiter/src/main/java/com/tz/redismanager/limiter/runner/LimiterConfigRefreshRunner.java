package com.tz.redismanager.limiter.runner;

import com.tz.redismanager.limiter.config.LimiterProperties;
import com.tz.redismanager.limiter.service.ILimiterConfigService;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p>限流器配置更新Runner</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 22:02
 **/
@Component
public class LimiterConfigRefreshRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LimiterProperties limiterProperties;

    @Autowired
    private ILimiterConfigService limiterConfigService;

    @Autowired
    private ILimiterService limiterService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        limiterProperties.getLimiters().forEach((key, value) -> {
            limiterConfigService.add(value);
            limiterService.resetLimiter(value);
            logger.info("[限流器配置更新和重新初始化] [{}] [{}] [完成]", value.getKey(), value.getName());
        });
    }
}
