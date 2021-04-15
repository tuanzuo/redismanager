package com.tz.redismanager.config.notify;

import com.tz.redismanager.config.domain.po.ConfigPO;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:19
 **/
public interface INotiyService {

     void add(ConfigPO po);

     void update(ConfigPO po);
}
