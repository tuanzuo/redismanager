package com.tz.redismanager.configure;

import com.tz.redismanager.cacher.config.CacheEvictConfig;
import com.tz.redismanager.cacher.config.CacheableConfig;
import com.tz.redismanager.cacher.service.ICacheService;
import com.tz.redismanager.cacher.service.ICacherConfigService;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.service.ConfigChangeServiceAdapter;
import com.tz.redismanager.config.util.JsonUtils;
import com.tz.redismanager.limiter.config.LimiterConfig;
import com.tz.redismanager.limiter.service.ILimiterConfigService;
import com.tz.redismanager.limiter.service.ILimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-12 22:56
 **/
@Service
public class ConfigChangeServiceImpl extends ConfigChangeServiceAdapter {

    @Autowired
    private ICacherConfigService cacherConfigService;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private ILimiterConfigService limiterConfigService;
    @Autowired
    private ILimiterService limiterService;

    @Override
    public void doCacherEnableChange(ConfigPO po) {
        CacheableConfig config = JsonUtils.parseObject(po.getContent(), CacheableConfig.class);
        cacherConfigService.addCacheableConfig(config);
        cacheService.resetCacher(config);
    }

    @Override
    public void doCacherEvictChange(ConfigPO po) {
        CacheEvictConfig config = JsonUtils.parseObject(po.getContent(), CacheEvictConfig.class);
        cacherConfigService.addCacheEvictConfig(config);
    }

    @Override
    public void doLimiterChange(ConfigPO po) {
        LimiterConfig config = JsonUtils.parseObject(po.getContent(), LimiterConfig.class);
        limiterConfigService.addLimiterConfig(config);
        limiterService.resetLimiter(config);
    }

}
