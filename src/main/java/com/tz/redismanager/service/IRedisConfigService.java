package com.tz.redismanager.service;

import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;

import java.util.List;

public interface IRedisConfigService {

    /**
     * 查询redis连接配置
     *
     * @param searchKey
     * @return
     */
    List<RedisConfigPO> searchList(String searchKey);

    /**
     * 添加redis连接配置
     *
     * @param vo
     */
    void add(RedisConfigVO vo);

    /**
     * 删除redis连接配置
     *
     * @param id
     */
    void delete(String id);

    /**
     * 修改redis连接配置
     *
     * @param vo
     */
    void update(RedisConfigVO vo);
}
