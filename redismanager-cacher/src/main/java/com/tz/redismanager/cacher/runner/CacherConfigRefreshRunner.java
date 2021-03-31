package com.tz.redismanager.cacher.runner;

import com.tz.redismanager.cacher.config.CacherProperties;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.service.ICacherConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * <p>缓存器配置更新Runner</p>
 *
 * @author tuanzuo
 * @version 1.6.1
 * @time 2021-03-29 22:02
 **/
@Component
public class CacherConfigRefreshRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CacherProperties cacherProperties;

    @Autowired
    private ICacherConfigService cacherConfigService;

    @Autowired
    private ICacheService cacheService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cacherProperties.getCacheables().forEach((key, value) -> {
            cacherConfigService.addCacheableConfig(value);
            cacheService.resetCacher(value);
            logger.info("[缓存器配置更新和重新初始化] [{}] [{}] [完成]", value.getKey(), value.getName());
        });
        cacherProperties.getCacheEvicts().forEach((key, value) -> {
            cacherConfigService.addCacheEvictConfig(value);
            logger.info("[缓存器失效配置更新] [{}] [{}] [完成]", value.getKey(), value.getName());
        });
    }
}
