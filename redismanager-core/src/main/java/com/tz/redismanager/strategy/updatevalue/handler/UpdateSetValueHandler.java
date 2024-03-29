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

/**
 * 修改set的value处理器
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.SET})
public class UpdateSetValueHandler extends AbstractUpdateValueHandler {

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //set类型修改value
        Set<String> oldValues = JsonUtils.parseObject(vo.getOldStringValue(), SET_STRING_TYPE);
        Set<String> newValues = JsonUtils.parseObject(vo.getStringValue(), SET_STRING_TYPE);
        oldValues = Optional.ofNullable(oldValues).orElse(new HashSet<>());
        newValues = Optional.ofNullable(newValues).orElse(new HashSet<>());
        List<String> delValues = (List<String>) CollectionUtils.removeAll(oldValues, newValues);
        List<String> addValues = (List<String>) CollectionUtils.removeAll(newValues, oldValues);
        if (CollectionUtils.isNotEmpty(delValues)) {
            redisTemplate.opsForSet().remove(vo.getKey(), delValues.toArray());
        }
        if (CollectionUtils.isNotEmpty(addValues)) {
            redisTemplate.opsForSet().add(vo.getKey(), addValues.toArray());
        }
        //设置过期时间
        this.setKeyExpireTime(redisTemplate, vo.getKey(), redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}