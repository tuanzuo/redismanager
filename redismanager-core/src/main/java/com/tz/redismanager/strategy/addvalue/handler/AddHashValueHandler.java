package com.tz.redismanager.strategy.addvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisKeyAddVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.strategy.addvalue.AbstractAddValueHandler;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 添加hash的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.HASH})
public class AddHashValueHandler extends AbstractAddValueHandler {

    @Override
    public Object handle(RedisKeyAddVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //hash类型添加
        Map<String, String> newValues = JsonUtils.parseObject(vo.getStringValue(), HASH_STRING_TYPE);
        if (MapUtils.isNotEmpty(newValues)) {
            redisTemplate.opsForHash().putAll(vo.getKey(), newValues);
            //设置过期时间
            this.setKeyExpireTime(redisTemplate, vo.getKey(), vo.getExpireTime());
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }


}
