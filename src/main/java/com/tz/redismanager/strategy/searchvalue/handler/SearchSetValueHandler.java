package com.tz.redismanager.strategy.searchvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.strategy.searchvalue.AbstractSearchValueHandler;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
@HandlerType({HandlerTypeEnum.SET})
public class SearchSetValueHandler extends AbstractSearchValueHandler {
    private static final Logger logger = LoggerFactory.getLogger(SearchSetValueHandler.class);

    @Override
    public Object handle(RedisValueQueryVo vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        Object value = null;
        try {
            //value = redisTemplate.opsForSet().members(vo.getSearchKey());
            value = this.getValue(vo, redisTemplate);
            if (null == value) {
                //重新设置keySerializer
                this.reSetKeySerializer(redisTemplate);
                //value = redisTemplate.opsForSet().members(vo.getSearchKey());
                value = this.getValue(vo, redisTemplate);
            }
        } catch (Exception e) {
            logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
            logger.info("[RedisAdmin] [searchKeyValue] {ValueSerializer从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
            redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
            //value = redisTemplate.opsForSet().members(vo.getSearchKey());
            value = this.getValue(vo, redisTemplate);
        }
        return value;
    }

    private Object getValue(RedisValueQueryVo vo, RedisTemplate<String, Object> redisTemplate) {
        Set<Object> set = new HashSet<>();
        try (Cursor<Object> cursor = redisTemplate.opsForSet().scan(vo.getSearchKey(), ScanOptions.scanOptions().match("*").count(1000).build())) {
            while (cursor.hasNext()) {
                set.add(cursor.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CollectionUtils.isNotEmpty(set) ? set : null;
    }
}
