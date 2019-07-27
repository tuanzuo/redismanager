package com.tz.redismanager.service;

import com.tz.redismanager.bean.vo.*;

import java.util.List;

public interface IRedisAdminService {

    List<RedisTreeNode> searchKey(String id, String key);

    RedisValueResp searchKeyValue(RedisValueQueryVo vo);

    void delKeys(RedisKeyDelVo vo);

    void renameKey(RedisKeyUpdateVo vo);

    void setTtl(RedisKeyUpdateVo vo);

    void updateValue(RedisKeyUpdateVo vo);

}
