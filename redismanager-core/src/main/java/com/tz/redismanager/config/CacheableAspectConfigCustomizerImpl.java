package com.tz.redismanager.config;

import com.tz.redismanager.cacher.aspect.ICacheableAspectConfigCustomizer;
import org.springframework.stereotype.Service;

/**
 * <p>自定义缓存生效切面的配置接口的实现</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-13 23:41
 **/
@Service
public class CacheableAspectConfigCustomizerImpl implements ICacheableAspectConfigCustomizer {

    @Override
    public int customizeOrder() {
        return 200;
    }
}
