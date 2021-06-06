package com.tz.redismanager.strategy.queryvalue;

import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.strategy.IHandler;

/**
 * 查询redis key对应value的接口
 *
 * @version 1.0
 * @time 2019-06-23 21:22
 **/
public interface IQueryValueHandler extends IHandler<RedisValueQueryVO, Object> {
}
