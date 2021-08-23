package com.tz.redismanager.strategy.addvalue.handler;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.RedisKeyAddVO;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.strategy.addvalue.AbstractAddValueHandler;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 添加set的value处理器
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-07 17:38:18
 **/
@Service
@HandlerType({HandlerTypeEnum.SET})
public class AddSetValueHandler extends AbstractAddValueHandler {

    @Override
    public Object handle(RedisKeyAddVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //set类型添加
        Set<String> newValues = JsonUtils.parseObject(vo.getStringValue(), SET_STRING_TYPE);
        if (CollectionUtils.isNotEmpty(newValues)) {
            redisTemplate.opsForSet().add(vo.getKey(), newValues.toArray());
            //设置过期时间
            this.setKeyExpireTime(redisTemplate, vo.getKey(), vo.getExpireTime());
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

}
