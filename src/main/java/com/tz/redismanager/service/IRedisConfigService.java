package com.tz.redismanager.service;

import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;

import java.util.List;

public interface IRedisConfigService {

    /**
     * 查询redis连接配置
     *
     * @param searchKey
     * @param pageNum 查询的页数
     * @param pagesize 每页大小
     * @return
     */
    List<RedisConfigPO> searchList(String searchKey, Integer pageNum, Integer pagesize);

    /**
     * 添加redis连接配置
     *
     * @param vo
     */
    void add(RedisConfigVO vo, String token);

    /**
     * 删除redis连接配置
     *
     * @param id
     */
    void delete(String id, String token);

    /**
     * 修改redis连接配置
     *
     * @param vo
     */
    void update(RedisConfigVO vo, String token);
}
