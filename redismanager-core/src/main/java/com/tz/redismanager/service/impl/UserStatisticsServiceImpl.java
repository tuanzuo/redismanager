package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.service.IRedisTemplateExtService;
import com.tz.redismanager.service.IUserStatisticsService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>用户统计接口实现</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:38
 **/
@Service
public class UserStatisticsServiceImpl implements IUserStatisticsService {

    private static final String YYYYMMDD = "yyyyMMdd";
    private static final Integer HOURS_24 = 24;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IRedisTemplateExtService redisTemplateExtService;

    @Override
    public void addOnlineUser(Integer userId) {
        if (null == userId) {
            return;
        }
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + new DateTime().toString(YYYYMMDD);
        stringRedisTemplate.opsForValue().setBit(onlineKey, userId, true);
        stringRedisTemplate.expire(onlineKey, HOURS_24, TimeUnit.HOURS);
    }

    @Override
    public void removeOnlineUser(Integer userId) {
        if (null == userId) {
            return;
        }
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + new DateTime().toString(YYYYMMDD);
        stringRedisTemplate.opsForValue().setBit(onlineKey, userId, false);
    }

    @Override
    public Long countOnlineUser() {
        String onlineKey = ConstInterface.CacheKey.USER_ONLINE + new DateTime().toString(YYYYMMDD);
        Long count = redisTemplateExtService.bitCount(onlineKey);
        return null == count ? 0 : count;
    }
}
