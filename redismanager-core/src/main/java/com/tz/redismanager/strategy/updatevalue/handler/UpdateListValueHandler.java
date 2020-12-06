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

    private static final String LIST_REMOVE_VALUE = "RM_LIST_REMOVE_VALUE";

    @Override
    public Object handle(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //list类型修改value
        List<String> oldValues = JsonUtils.parseObject(vo.getOldStringValue(), LIST_STRING_TYPE);
        List<String> newValues = JsonUtils.parseObject(vo.getStringValue(), LIST_STRING_TYPE);
        oldValues = Optional.ofNullable(oldValues).orElse(new ArrayList<>());
        newValues = Optional.ofNullable(newValues).orElse(new ArrayList<>());
        if (CollectionUtils.isEmpty(newValues)) {
            this.handleEmptyList(vo, oldValues);
        } else {
            this.handleNotEmptyList(vo, oldValues, newValues);
        }
        //删除数据
        redisTemplate.opsForList().remove(vo.getKey(), 0, LIST_REMOVE_VALUE);
        //设置过期时间
        this.setKeyExpireTime(redisTemplate, vo.getKey(), redisTemplate.getExpire(vo.getKey()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private void handleEmptyList(RedisKeyUpdateVO vo, List<String> oldValues) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //新List为空，直接将之前List对应的index设置为需要被删除的value
        for (int i = 0, index = vo.getStart().intValue(); i < oldValues.size(); i++, index++) {
            redisTemplate.opsForList().set(vo.getKey(), index, LIST_REMOVE_VALUE);
        }
    }

    private void handleNotEmptyList(RedisKeyUpdateVO vo, List<String> oldValues, List<String> newValues) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        int oldSize = oldValues.size();
        int newSize = newValues.size();
        int maxSize = Math.max(oldSize, newSize);
        String preValue = "";
        for (int i = 0, index = vo.getStart().intValue(); i < maxSize; i++, index++) {
            if (oldSize <= newSize) {
                if (i < oldSize) {
                    //把之前List对应的index的value设置为新List的value
                    redisTemplate.opsForList().set(vo.getKey(), index, newValues.get(i));
                } else {
                    /**新加的value都添加到列表的右边(尾部)*/
                    redisTemplate.opsForList().rightPush(vo.getKey(), newValues.get(i));
                    /*if (oldSize == 0) {
                        //之前List为空，就直接将新List的value添加到集合的右边
                        redisTemplate.opsForList().rightPush(vo.getKey(), newValues.get(i));
                    } else {
                        //将新List的value添加到前一个value的后面
                        redisTemplate.opsForList().rightPush(vo.getKey(), preValue, newValues.get(i));
                    }*/
                }
            } else {
                if (i < newSize) {
                    //把之前List对应的index的value设置为新List的value
                    redisTemplate.opsForList().set(vo.getKey(), index, newValues.get(i));
                } else {
                    //把之前list对应index的位置设置成需要删除的value
                    redisTemplate.opsForList().set(vo.getKey(), index, LIST_REMOVE_VALUE);
                }
            }
            if (i < newSize) {
                preValue = newValues.get(i);
            }
        }
    }

}