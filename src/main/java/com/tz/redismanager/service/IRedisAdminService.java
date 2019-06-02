package com.tz.redismanager.service;

import com.tz.redismanager.bean.vo.RedisKeyDelVo;
import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.bean.vo.RedisTreeNode;
import com.tz.redismanager.bean.vo.RedisValueResp;

import java.util.List;

public interface IRedisAdminService {

    List<RedisTreeNode> searchKey(String id, String key);

    RedisValueResp searchKeyValue(String id, RedisValueQueryVo vo);

    void delKeys(String id, RedisKeyDelVo vo);
}
