package com.tz.redismanager.config.notify;

import com.tz.redismanager.config.domain.po.ConfigPO;

/**
 * <p>通知服务接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:19
 **/
public interface INotiyService {

     /**
      * 添加配置时触发通知
      * @param po
      */
     void add(ConfigPO po);

     /**
      * 更新配置时触发通知
      * @param po
      */
     void update(ConfigPO po);
}
