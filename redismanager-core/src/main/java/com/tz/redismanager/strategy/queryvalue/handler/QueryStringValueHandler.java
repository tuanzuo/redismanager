package com.tz.redismanager.strategy.queryvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.strategy.queryvalue.AbstractQueryValueHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.RedisContextUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 查询string的value处理器
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
@HandlerType({HandlerTypeEnum.STRING})
public class QueryStringValueHandler extends AbstractQueryValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(QueryStringValueHandler.class);

    @Override
    public Object handle(RedisValueQueryVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        Object value = null;
        try {
            value = redisTemplate.opsForValue().get(vo.getSearchKey());
            if (null == value) {
                //重新设置keySerializer
                this.reSetKeySerializer(redisTemplate);
                value = redisTemplate.opsForValue().get(vo.getSearchKey());
            }
        } catch (Exception e) {
            logger.error("{id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
            logger.info("{ValueSerializer从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
            //redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
            //重新设置keySerializer
            this.reSetKeySerializer(redisTemplate);
            value = redisTemplate.opsForValue().get(vo.getSearchKey());
        }
        return value;
    }
}
