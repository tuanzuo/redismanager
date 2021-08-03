package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.po.ConfigPO;

/**
 * <p>配置更新服务接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-11 22:37
 **/
public interface IConfigChangeService {

    void change(ConfigPO po);
}
