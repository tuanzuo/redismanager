package com.tz.redismanager.config.sdk.service;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.enm.ConfigTypeEnum;

/**
 * <p>抽象的配置更新服务</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
public abstract class AbstractConfigChangeService implements IConfigChangeService {

    @Override
    public void change(ConfigContext context) {
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.getByCode(context.getConfigType());
        switch (configTypeEnum) {
            case CACHER_ENABLE:
                doCacherEnableChange(context);
                break;
            case CACHER_EVICT:
                doCacherEvictChange(context);
                break;
            case LIMITER:
                doLimiterChange(context);
                break;
            case TOKEN:
                doTokenChange(context);
                break;
            default:
                doOtherChange(context);
                break;
        }
    }

    public abstract void doCacherEnableChange(ConfigContext context);

    public abstract void doCacherEvictChange(ConfigContext context);

    public abstract void doLimiterChange(ConfigContext context);

    public abstract void doTokenChange(ConfigContext context);

    public abstract void doOtherChange(ConfigContext context);

}
