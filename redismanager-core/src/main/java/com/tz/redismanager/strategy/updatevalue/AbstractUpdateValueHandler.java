package com.tz.redismanager.strategy.updatevalue;

import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.domain.vo.RedisKeyUpdateVO;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.strategy.AbstractHandler;

/**
 * 抽象的修改redis key的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@StrategyType({StrategyTypeEnum.UPDATE_VALUE})
public abstract class AbstractUpdateValueHandler extends AbstractHandler<RedisKeyUpdateVO, Object> implements IUpdateValueHandler {

}
