package com.tz.redismanager.service;


import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.bean.ApiResult;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.RedisConfigVO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public interface IRedisContextService {

    /**
     * 初始化RedisTemplate,非线程安全
     * @param id
     * @return
     */
    RedisTemplate<String, Object> initContext(String id);

    RedisTemplate<String, Object> getRedisTemplate(String id);

    Map<String, RedisTemplate<String, Object>> getRedisTemplateMap();

    void removeRedisTemplate(String id);

    LoadingCache<String, RedisConfigPO> getRedisConfigCache();

    /**
     * 测试redis连接
     * @param vo
     * @return
     */
    ApiResult<String> testRedisConnection(RedisConfigVO vo);
}
