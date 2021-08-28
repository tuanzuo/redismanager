package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.*;

import java.util.List;

/**
 * redis数据管理
 *
 * @author tuanzuo
 */
public interface IRedisAdminService {

    /**
     * 查询redis服务器信息 v1.7.0
     * @param id
     * @return
     */
    ApiResult<RedisServerInfoVO> queryServerInfo(Long id);

    /**
     * 查询redis的key
     *
     * @param id
     * @param key
     * @return
     */
    List<RedisTreeNode> searchKey(Long id, String key);

    /**
     * 查询redis key对应的value
     *
     * @param vo
     * @return
     */
    RedisValueResp searchKeyValue(RedisValueQueryVO vo);

    /**
     * 删除redis key
     *
     * @param vo
     */
    ApiResult<?> delKeys(RedisKeyDelVO vo);

    /**
     * 重命名redis key
     *
     * @param vo
     */
    ApiResult<?> renameKey(RedisKeyUpdateVO vo);

    /**
     * 设置redis key的过期时间
     *
     * @param vo
     */
    ApiResult<?> setTtl(RedisKeyUpdateVO vo);

    /**
     * 更新redis key的value
     *
     * @param vo
     */
    ApiResult<?> updateValue(RedisKeyUpdateVO vo);

    /**
     * 添加redis key
     *
     * @param vo
     */
    ApiResult<?> addKey(RedisKeyAddVO vo);
}
