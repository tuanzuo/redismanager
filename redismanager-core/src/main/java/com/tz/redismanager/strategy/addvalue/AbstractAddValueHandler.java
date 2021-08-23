package com.tz.redismanager.strategy.addvalue;

import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.domain.vo.RedisKeyAddVO;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.strategy.AbstractHandler;

/**
 * 抽象的添加redis key的value处理器
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@StrategyType({StrategyTypeEnum.ADD_VALUE})
public abstract class AbstractAddValueHandler extends AbstractHandler<RedisKeyAddVO, Object> implements IAddValueHandler {

}
