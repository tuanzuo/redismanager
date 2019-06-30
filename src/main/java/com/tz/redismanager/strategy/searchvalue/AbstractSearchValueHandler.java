package com.tz.redismanager.strategy.searchvalue;

import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.strategy.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:40
 **/
@StrategyType({StrategyTypeEnum.SEARCH_VALUE})
public abstract class AbstractSearchValueHandler extends AbstractHandler<RedisValueQueryVo, Object> implements ISearchValueHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSearchValueHandler.class);

    /**
     * 重新设置keySerializer
     *
     * @param redisTemplate
     */
    public void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        if (null != keySerializer) {
            if (keySerializer.getClass().getName().equals(StringRedisSerializer.class.getName())) {
                redisTemplate.setKeySerializer(redisTemplate.getDefaultSerializer());
            } else {
                redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
            }
            logger.info("[RedisAdmin] [reSetKeySerializer] {keySerializer从{}切换为:{}再查询}", keySerializer.getClass().getSimpleName(), redisTemplate.getKeySerializer().getClass().getSimpleName());
        }
    }
}
