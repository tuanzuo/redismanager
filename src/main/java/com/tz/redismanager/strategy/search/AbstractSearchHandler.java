package com.tz.redismanager.strategy.search;

import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.strategy.AbstractHandler;
import com.tz.redismanager.strategy.IHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:40
 **/
public abstract class AbstractSearchHandler extends AbstractHandler<RedisValueQueryVo, Object> implements ISearchValueHandler {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSearchHandler.class);

    private static Map<Object, ISearchValueHandler> handlerMap = new HashMap<>();

    @Override
    public IHandler getHandler(Object handlerType) {
        return handlerMap.get(handlerType);
    }

    @Override
    public void putHandlerToMap(Object handlerType, IHandler handler) {
        handlerMap.put(handlerType, (ISearchValueHandler) handler);
    }

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
