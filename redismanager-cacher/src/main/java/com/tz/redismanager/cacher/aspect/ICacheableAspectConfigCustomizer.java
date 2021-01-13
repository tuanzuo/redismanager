package com.tz.redismanager.cacher.aspect;

/**
 * <p>自定义缓存生效切面的配置接口</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-13 23:32
 **/
public interface ICacheableAspectConfigCustomizer {

    /**
     * 自定义切面的Order
     */
    int customizeOrder();

}
