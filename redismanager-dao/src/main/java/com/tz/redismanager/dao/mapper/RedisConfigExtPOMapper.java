package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;

import java.util.List;

/**
 * <p>redis连接配置扩展Mapper</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:19
 **/
public interface RedisConfigExtPOMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RedisConfigExtPO record);

    int insertSelective(RedisConfigExtPO record);

    RedisConfigExtPO selectByPrimaryKey(Long id);

    List<RedisConfigExtPO> selectList(RedisConfigExtPO record);

    int updateByPrimaryKeySelective(RedisConfigExtPO record);

    int updateByPrimaryKey(RedisConfigExtPO record);
}