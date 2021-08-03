package com.tz.redismanager.strategy.queryvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.vo.RedisValueQueryVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.strategy.queryvalue.AbstractQueryValueHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 查询hash的value处理器
 *
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
@HandlerType({HandlerTypeEnum.HASH})
public class QueryHashValueHandler extends AbstractQueryValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(QueryHashValueHandler.class);

    @Override
    public Object handle(RedisValueQueryVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        Object value = null;
        try {
            value = this.getValue(vo, redisTemplate);
            if (null == value) {
                //重新设置keySerializer
                this.reSetKeySerializer(redisTemplate);
                value = this.getValue(vo, redisTemplate);
            }
        } catch (Exception e) {
            logger.error("{id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
            logger.info("{ValueSerializer从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
            logger.info("{HashValueSerializer从{}切换到StringRedisSerializer处理}", redisTemplate.getHashValueSerializer().getClass().getSimpleName());
            redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
            redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
            value = this.getValue(vo, redisTemplate);
        }
        return value;
    }

    private Object getValue(RedisValueQueryVO vo, RedisTemplate<String, Object> redisTemplate) {
        Map<Object, Object> map = new HashMap<>();
        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(vo.getSearchKey(), ScanOptions.scanOptions().match(ConstInterface.Symbol.STAR).count(1000).build())) {
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> entry = cursor.next();
                map.put(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return MapUtils.isNotEmpty(map) ? map : null;
    }
}
