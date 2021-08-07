package com.tz.redismanager.config.sdk.service;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;

/**
 * <p>配置更新服务接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
public interface IConfigChangeService {

    void change(ConfigContext context);
}
