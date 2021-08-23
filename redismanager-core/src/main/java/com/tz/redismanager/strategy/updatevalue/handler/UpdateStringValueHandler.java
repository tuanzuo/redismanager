package com.tz.redismanager.strategy.updatevalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisKeyUpdateVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.strategy.updatevalue.AbstractUpdateValueHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

/**
 * 修改string的value处理器
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.STRING})
public class UpdateStringValueHandler extends AbstractUpdateValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(UpdateStringValueHandler.class);

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //string类型修改value
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
        //1、先试用string序列化方式把value存入redis
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        this.setValueAndExpireTimeForStringType(vo.getKey(), vo.getStringValue(), redisTemplate, redisTemplate.getExpire(vo.getKey()));
        if (StringRedisSerializer.class.equals(valueSerializer.getClass())) {
            logger.info("[{更新Key的Value完成:{}}", JsonUtils.toJsonStr(vo));
            return new ApiResult<>(ResultCode.SUCCESS);
        }
        //2、再查询出value,最后使用redisTemplate本身的序列化方式把数据存入redis
        Object valueTemp = redisTemplate.opsForValue().get(vo.getKey());
        redisTemplate.setValueSerializer(valueSerializer);
        this.setValueAndExpireTimeForStringType(vo.getKey(), valueTemp, redisTemplate, redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}