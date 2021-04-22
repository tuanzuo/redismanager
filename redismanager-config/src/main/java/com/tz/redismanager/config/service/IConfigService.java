package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.po.ConfigPO;

import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 22:40
 **/
public interface IConfigService {

    List<ConfigPO> queryList(ConfigPageParam param);

    int count(ConfigPageParam param);

    void addConfig(ConfigPO configPO);

    void updateConfig(ConfigPO configPO);
}
