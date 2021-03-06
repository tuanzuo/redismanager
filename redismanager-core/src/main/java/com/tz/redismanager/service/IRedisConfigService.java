package com.tz.redismanager.service;

import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.security.domain.AuthContext;

import java.util.List;

public interface IRedisConfigService {

    /**
     * 查询redis连接配置
     *
     * @return
     */
    List<RedisConfigPO> searchList(RedisConfigPageParam param);

    /**
     * 查询redis连接配置
     */
    RedisConfigPO query(String id);

    /**
     * 查询redis连接配置分析页数据
     * @return
     */
    List<RedisConfigAnalysisDTO> queryRedisConfigAnalysis();

    /**
     * 失效redis连接配置缓存
     */
    void invalidateCache(String id);

    /**
     * 添加redis连接配置
     *
     * @param vo
     */
    RedisConfigPO add(RedisConfigVO vo, AuthContext authContext);

    /**
     * 删除redis连接配置
     *
     * @param id
     */
    ApiResult<?> delete(String id, AuthContext authContext);

    /**
     * 修改redis连接配置
     *
     * @param vo
     */
    ApiResult<?> update(RedisConfigVO vo, AuthContext authContext);
}
