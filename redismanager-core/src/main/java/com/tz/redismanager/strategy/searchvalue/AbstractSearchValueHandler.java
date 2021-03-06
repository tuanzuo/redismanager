package com.tz.redismanager.strategy.searchvalue;

import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.strategy.AbstractHandler;
import com.tz.redismanager.util.CommonUtils;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 抽象的查询redis key的value处理器
 *
 * @version 1.0
 * @time 2019-06-23 21:40
 **/
@StrategyType({StrategyTypeEnum.SEARCH_VALUE})
public abstract class AbstractSearchValueHandler extends AbstractHandler<RedisValueQueryVO, Object> implements ISearchValueHandler {

    /**
     * 重新设置keySerializer
     *
     * @param redisTemplate
     */
    public void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        CommonUtils.reSetKeySerializer(redisTemplate);
    }
}
