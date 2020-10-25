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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 修改zset的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.ZSET})
public class UpdateZsetValueHandler extends AbstractUpdateValueHandler {

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //zset类型修改value
        Set<ZSetValue> oldValues = JsonUtils.parseObject(vo.getOldStringValue(), ZSET_STRING_TYPE);
        Set<ZSetValue> newValues = JsonUtils.parseObject(vo.getStringValue(), ZSET_STRING_TYPE);
        oldValues = Optional.ofNullable(oldValues).orElse(new HashSet<>());
        newValues = Optional.ofNullable(newValues).orElse(new HashSet<>());

        Set<String> oldMembers = oldValues.stream().map(temp -> temp.getValue()).collect(Collectors.toSet());
        Set<String> newMembers = newValues.stream().map(temp -> temp.getValue()).collect(Collectors.toSet());

        List<String> delMembers = (List<String>) CollectionUtils.removeAll(oldMembers, newMembers);
        if (CollectionUtils.isNotEmpty(delMembers)) {
            redisTemplate.opsForZSet().remove(vo.getKey(), delMembers.toArray());
        }
        if (CollectionUtils.isNotEmpty(newValues)) {
            newValues.forEach(temp -> {
                redisTemplate.opsForZSet().add(vo.getKey(), temp.getValue(), temp.getScore());
            });
        }
        //设置过期时间
        this.setKeyExpireTime(redisTemplate, vo.getKey(), redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}