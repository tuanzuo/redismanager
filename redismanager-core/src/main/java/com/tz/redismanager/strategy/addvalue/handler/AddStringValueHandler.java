package com.tz.redismanager.strategy.addvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisKeyAddVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.strategy.addvalue.AbstractAddValueHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * 添加string的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.STRING})
public class AddStringValueHandler extends AbstractAddValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(AddStringValueHandler.class);

    @Override
    public Object handle(RedisKeyAddVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //string类型添加
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        //1、先试用string序列化方式把value存入redis
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.setValueAndExpireTimeForStringType(vo.getKey(), vo.getStringValue(), redisTemplate, vo.getExpireTime());
        if (StringRedisSerializer.class.equals(valueSerializer.getClass())) {
            logger.info("{添加Key完成:{}}", JsonUtils.toJsonStr(vo));
            return new ApiResult<>(ResultCode.SUCCESS);
        }
        //2、再查询出value,最后使用redisTemplate本身的序列化方式把数据存入redis
        Object valueTemp = redisTemplate.opsForValue().get(vo.getKey());
        redisTemplate.setValueSerializer(valueSerializer);
        this.setValueAndExpireTimeForStringType(vo.getKey(), valueTemp, redisTemplate, vo.getExpireTime());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}
