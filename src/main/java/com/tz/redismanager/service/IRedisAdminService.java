package com.tz.redismanager.service;

import com.tz.redismanager.bean.vo.*;

import java.util.List;

public interface IRedisAdminService {

    List<RedisTreeNode> searchKey(String id, String key);

    RedisValueResp searchKeyValue(String id, RedisValueQueryVo vo);

    void delKeys(String id, RedisKeyDelVo vo);

    void renameKey(String id, RedisKeyUpdateVo vo);

    void setTtl(String id, RedisKeyUpdateVo vo);


}
