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
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 添加list的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.LIST})
public class AddListValueHandler extends AbstractAddValueHandler {

    private static final Logger logger = TraceLoggerFactory.getLogger(AddListValueHandler.class);

    @Override
    public Object handle(RedisKeyAddVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //过期时间
        Long expireTime = vo.getExpireTime();
        //list类型添加
        List<String> newValues = JsonUtils.parseObject(vo.getStringValue(), LIST_STRING_TYPE);
        if (CollectionUtils.isNotEmpty(newValues)) {
            redisTemplate.opsForList().rightPushAll(vo.getKey(), newValues.toArray());
        }
        //设置过期时间
        if (null != expireTime && -1 != expireTime) {
            redisTemplate.expire(vo.getKey(), expireTime, TimeUnit.SECONDS);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}
