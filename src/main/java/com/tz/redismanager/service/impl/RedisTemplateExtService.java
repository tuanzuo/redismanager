package com.tz.redismanager.service.impl;

import com.tz.redismanager.service.IRedisTemplateExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>RedisTemplate扩展服务</p>
 *
 * @version 1.4.0
 * @time 2020-10-12 22:53
 **/
@Service
public class RedisTemplateExtService implements IRedisTemplateExtService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long bitCount(String key) {
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(key.getBytes());
            }
        });
    }
}
