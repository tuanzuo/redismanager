package com.tz.redismanager.config.sdk.service;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.enm.ConfigTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>抽象的配置更新服务</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
public abstract class AbstractConfigChangeService implements IConfigChangeService {

    private final Logger logger = LoggerFactory.getLogger(AbstractConfigChangeService.class);

    @Override
    public void change(ConfigContext context) {
        ConfigTypeEnum configTypeEnum = ConfigTypeEnum.getByCode(context.getConfigType());
        switch (configTypeEnum) {
            case CACHER_ENABLE:
                this.doCacherEnableChange(context);
                logger.info("[ConfigSdk配置] [缓存生效配置更新完成] key={} -- keyName={}", context.getConfigKey(), context.getKeyName());
                break;
            case CACHER_EVICT:
                this.doCacherEvictChange(context);
                logger.info("[ConfigSdk配置] [缓存失效配置更新完成] key={} -- keyName={}", context.getConfigKey(), context.getKeyName());
                break;
            case LIMITER:
                this.doLimiterChange(context);
                logger.info("[ConfigSdk配置] [限流配置更新完成] key={} -- keyName={}", context.getConfigKey(), context.getKeyName());
                break;
            case TOKEN:
                this.doTokenChange(context);
                logger.info("[ConfigSdk配置] [Token配置更新完成] key={} -- keyName={}", context.getConfigKey(), context.getKeyName());
                break;
            default:
                this.doOtherChange(context);
                logger.warn("[ConfigSdk配置] [其他配置更新完成] key={} -- keyName={}", context.getConfigKey(), context.getKeyName());
                break;
        }
    }

    /**
     * 缓存生效配置更新处理
     *
     * @param context
     */
    public abstract void doCacherEnableChange(ConfigContext context);

    /**
     * 缓存失效配置更新处理
     *
     * @param context
     */
    public abstract void doCacherEvictChange(ConfigContext context);

    /**
     * 限流配置更新处理
     *
     * @param context
     */
    public abstract void doLimiterChange(ConfigContext context);

    /**
     * Token配置更新处理
     *
     * @param context
     */
    public abstract void doTokenChange(ConfigContext context);

    /**
     * 其他配置更新处理
     *
     * @param context
     */
    public abstract void doOtherChange(ConfigContext context);

}
