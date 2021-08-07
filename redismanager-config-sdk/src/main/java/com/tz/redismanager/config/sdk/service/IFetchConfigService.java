package com.tz.redismanager.config.sdk.service;

import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.domain.dto.ConfigQueryDTO;

import java.util.List;

/**
 * <p>拉取配置接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:21
 **/
public interface IFetchConfigService {

    /**
     * 初始化时拉取配置
     * @param dto
     * @return
     */
    List<ConfigContext> fetchConfig(ConfigQueryDTO dto);

}
