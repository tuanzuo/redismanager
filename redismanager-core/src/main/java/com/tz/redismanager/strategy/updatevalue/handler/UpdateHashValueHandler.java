package com.tz.redismanager.strategy.updatevalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisKeyUpdateVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.strategy.updatevalue.AbstractUpdateValueHandler;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 修改hash的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.HASH})
public class UpdateHashValueHandler extends AbstractUpdateValueHandler {

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //hash类型修改value
        Map<String, String> oldValues = JsonUtils.parseObject(vo.getOldStringValue(), HASH_STRING_TYPE);
        Map<String, String> newValues = JsonUtils.parseObject(vo.getStringValue(), HASH_STRING_TYPE);
        oldValues = Optional.ofNullable(oldValues).orElse(new HashMap<>());
        newValues = Optional.ofNullable(newValues).orElse(new HashMap<>());
        Set<String> oldKeys = oldValues.keySet();
        Set<String> newKeys = newValues.keySet();

        List<String> delValues = (List<String>) CollectionUtils.removeAll(oldKeys, newKeys);
        if (CollectionUtils.isNotEmpty(delValues)) {
            redisTemplate.opsForHash().delete(vo.getKey(), delValues.toArray());
        }
        if (MapUtils.isNotEmpty(newValues)) {
            redisTemplate.opsForHash().putAll(vo.getKey(), newValues);
        }
        //设置过期时间
        this.setKeyExpireTime(redisTemplate, vo.getKey(), redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}
