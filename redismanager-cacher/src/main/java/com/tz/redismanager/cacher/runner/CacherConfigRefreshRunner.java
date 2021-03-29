package com.tz.redismanager.cacher.runner;

import com.tz.redismanager.cacher.config.CacherProperties;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        cacherProperties.getCachers().forEach((key, value) -> {
            cacherConfigService.add(value);
            logger.info("[缓存器配置更新] [{}] [{}] [完成]", value.getKey(), value.getName());
        });
    }
}
