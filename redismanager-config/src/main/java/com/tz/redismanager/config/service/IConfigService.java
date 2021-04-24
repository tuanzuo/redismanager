package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
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

    List<ConfigPO> queryPageList(ConfigPageParam param);

    int count(ConfigQueryParam param);

    int countPage(ConfigPageParam param);

    int addConfig(ConfigPO configPO);

    int updateConfig(ConfigPO configPO);

    int delConfig(ConfigDTO dto);
}
