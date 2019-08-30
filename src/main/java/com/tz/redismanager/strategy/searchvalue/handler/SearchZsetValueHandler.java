package com.tz.redismanager.strategy.searchvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.strategy.searchvalue.AbstractSearchValueHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.RedisContextUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 查询zset的value处理器
 *
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
@HandlerType({HandlerTypeEnum.ZSET})
public class SearchZsetValueHandler extends AbstractSearchValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(SearchZsetValueHandler.class);

    @Override
    public Object handle(RedisValueQueryVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        Object value = null;
        try {
            //result = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE);
            value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
            if (null == value) {
                //重新设置keySerializer
                this.reSetKeySerializer(redisTemplate);
                value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
            }
        } catch (Exception e) {
            logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
            logger.info("[RedisAdmin] [searchKeyValue] {ValueSerializer从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
            redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
            value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
        }
        return value;
    }
}
