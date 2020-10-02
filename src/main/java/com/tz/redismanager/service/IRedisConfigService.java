package com.tz.redismanager.service;

import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.token.TokenContext;

import java.util.List;

public interface IRedisConfigService {

    /**
     * 查询redis连接配置
     *
     * @return
     */
    List<RedisConfigPO> searchList(RedisConfigPageParam param);

    /**
     * 添加redis连接配置
     *
     * @param vo
     */
    void add(RedisConfigVO vo, TokenContext tokenContext);

    /**
     * 删除redis连接配置
     *
     * @param id
     */
    void delete(String id, TokenContext tokenContext);

    /**
     * 修改redis连接配置
     *
     * @param vo
     */
    void update(RedisConfigVO vo, TokenContext tokenContext);
}
