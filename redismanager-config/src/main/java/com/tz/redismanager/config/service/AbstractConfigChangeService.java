package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.enm.ConfigTypeEnum;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-12 22:43
 **/
public abstract class AbstractConfigChangeService implements IConfigChangeService {

    @Override
    public void change(ConfigPO po) {
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.getByCode(po.getConfigType());
        switch (configTypeEnum) {
            case CACHER_ENABLE:
                doCacherEnableChange(po);
                break;
            case CACHER_EVICT:
                doCacherEvictChange(po);
                break;
            case LIMITER:
                doLimiterChange(po);
                break;
            case TOKEN:
                doTokenChange(po);
                break;
            default:
                doOtherChange(po);
                break;
        }
    }

    public abstract void doCacherEnableChange(ConfigPO po);

    public abstract void doCacherEvictChange(ConfigPO po);

    public abstract void doLimiterChange(ConfigPO po);

    public abstract void doTokenChange(ConfigPO po);

    public abstract void doOtherChange(ConfigPO po);


}
