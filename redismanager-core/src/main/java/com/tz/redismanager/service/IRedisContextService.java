package com.tz.redismanager.service;

import com.github.benmanes.caffeine.cache.LoadingCache;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

public interface IRedisContextService {

    /**
     * 初始化RedisTemplate,非线程安全
     *
     * @param id
     * @return
     */
    RedisTemplate<String, Object> initContext(String id);

    /**
     * 得到RedisTemplate
     *
     * @param id
     * @return
     */
    RedisTemplate<String, Object> getRedisTemplate(String id);

    /**
     * 得到RedisTemplate的Map
     *
     * @return
     */
    Map<String, RedisTemplate<String, Object>> getRedisTemplateMap();

    /**
     * 删除RedisTemplate
     *
     * @param id
     */
    void removeRedisTemplate(String id);

    /**
     * 得到redis连接配置的缓存
     *
     * @return
     */
    LoadingCache<String, RedisConfigPO> getRedisConfigCache();

    /**
     * 测试redis连接
     *
     * @param vo
     * @return
     */
    ApiResult<String> testRedisConnection(RedisConfigVO vo);
}
