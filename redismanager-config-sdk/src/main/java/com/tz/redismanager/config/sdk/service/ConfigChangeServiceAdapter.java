package com.tz.redismanager.config.sdk.service;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;

/**
 * <p>配置更新服务适配器</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
public abstract class ConfigChangeServiceAdapter extends AbstractConfigChangeService {

    @Override
    public void doCacherEnableChange(ConfigContext context) {

    }

    @Override
    public void doCacherEvictChange(ConfigContext context) {

    }

    @Override
    public void doLimiterChange(ConfigContext context) {

    }

    @Override
    public void doTokenChange(ConfigContext context) {

    }

    @Override
    public void doOtherChange(ConfigContext context) {

    }
}
