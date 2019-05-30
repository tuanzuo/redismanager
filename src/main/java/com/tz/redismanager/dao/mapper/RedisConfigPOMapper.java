package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.bean.po.RedisConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RedisConfigPOMapper {

    int deleteByPrimaryKey(String id);

    int insert(RedisConfigPO record);

    int insertSelective(RedisConfigPO record);

    RedisConfigPO selectByPrimaryKey(String id);

    List<RedisConfigPO> selectAll(@Param("searchKey") String searchKey);

    int updateByPrimaryKeySelective(RedisConfigPO record);

    int updateByPrimaryKey(RedisConfigPO record);
}