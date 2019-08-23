package com.tz.redismanager.service;

import com.tz.redismanager.domain.vo.*;

import java.util.List;

public interface IRedisAdminService {

    /**
     * 查询redis的key
     *
     * @param id
     * @param key
     * @return
     */
    List<RedisTreeNode> searchKey(String id, String key);

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
    void delKeys(RedisKeyDelVO vo);

    /**
     * 重命名redis key
     *
     * @param vo
     */
    void renameKey(RedisKeyUpdateVO vo);

    /**
     * 设置redis key的过期时间
     *
     * @param vo
     */
    void setTtl(RedisKeyUpdateVO vo);

    /**
     * 更新redis key的value
     *
     * @param vo
     */
    void updateValue(RedisKeyUpdateVO vo);

}
