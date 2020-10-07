package com.tz.redismanager.strategy.updatevalue;

import com.tz.redismanager.domain.vo.RedisKeyUpdateVO;
import com.tz.redismanager.strategy.IHandler;

/**
 * 修改redis key对应value的接口
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
public interface IUpdateValueHandler extends IHandler<RedisKeyUpdateVO, Object> {
}
