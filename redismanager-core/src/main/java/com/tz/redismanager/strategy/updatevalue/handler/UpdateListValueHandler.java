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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 修改list的value处理器
 *
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.LIST})
public class UpdateListValueHandler extends AbstractUpdateValueHandler {

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //list类型修改value
        List<String> oldValues = JsonUtils.parseObject(vo.getOldStringValue(), LIST_STRING_TYPE);
        List<String> newValues = JsonUtils.parseObject(vo.getStringValue(), LIST_STRING_TYPE);
        oldValues = Optional.ofNullable(oldValues).orElse(new ArrayList<>());
        newValues = Optional.ofNullable(newValues).orElse(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(oldValues)) {
            //删除旧数据
            //FIXME 由于目前查询List数据时只返回了前1000条数据，所以旧数据可以这样删除，当后面查询支持返回中间数据后就不能这样操作了
            redisTemplate.opsForList().trim(vo.getKey(), oldValues.size(), -1);
        }
        //FIXME 由于目前查询List数据时只返回了前1000条数据，所以新数据可以这样从头插入，当后面查询支持返回中间数据后就不能这样操作了
        if (CollectionUtils.isNotEmpty(newValues)) {
            Object[] newValueArray = newValues.toArray();
            CollectionUtils.reverseArray(newValueArray);
            redisTemplate.opsForList().leftPushAll(vo.getKey(), newValueArray);
        }
        //设置过期时间
        this.setKeyExpireTime(redisTemplate, vo.getKey(), redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}