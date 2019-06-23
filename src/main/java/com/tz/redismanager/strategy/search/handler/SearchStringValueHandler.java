package com.tz.redismanager.strategy.search.handler;

import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.enm.SearchValueHandlerEnum;
import com.tz.redismanager.strategy.search.AbstractSearchHandler;
import com.tz.redismanager.util.RedisContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
public class SearchStringValueHandler extends AbstractSearchHandler {

    private static final Logger logger = LoggerFactory.getLogger(SearchStringValueHandler.class);

    @Override
    public Object[] getHandlerType() {
        return new Object[]{SearchValueHandlerEnum.STRING};
    }

    @Override
    public Object handle(RedisValueQueryVo vo) {
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
            logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
            logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
            redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
            value = redisTemplate.opsForValue().get(vo.getSearchKey());
        }
        return value;
    }
}
