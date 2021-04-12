package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.po.ConfigPO;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-12 22:52
 **/
public abstract class ConfigChangeServiceAdapter extends AbstractConfigChangeService {

    @Override
    public void doCacherEnableChange(ConfigPO po) {

    }

    @Override
    public void doCacherEvictChange(ConfigPO po) {

    }

    @Override
    public void doLimiterChange(ConfigPO po) {

    }

    @Override
    public void doTokenChange(ConfigPO po) {

    }

    @Override
    public void doOtherChange(ConfigPO po) {

    }
}
