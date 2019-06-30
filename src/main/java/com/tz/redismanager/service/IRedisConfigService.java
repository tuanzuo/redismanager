package com.tz.redismanager.service;

import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.util.RsaException;

import java.util.List;

public interface IRedisConfigService {

    List<RedisConfigPO> searchList(String searchKey);

    void add(RedisConfigVO vo) throws RsaException;

    void delete(String id);

    void update(RedisConfigVO vo) throws RsaException;
}
