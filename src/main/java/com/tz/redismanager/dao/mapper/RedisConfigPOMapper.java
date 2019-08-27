package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.domain.po.RedisConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * redis连接配置Mapper
 *
 * @Since:2019-08-23 22:31:41
 * @Version:1.1.0
 */
@Mapper
public interface RedisConfigPOMapper {

    int deleteByPrimaryKey(String id);

    int insert(RedisConfigPO record);

    int insertSelective(RedisConfigPO record);

    RedisConfigPO selectByPrimaryKey(String id);

    List<RedisConfigPO> selectAll(@Param("searchKey") String searchKey, @Param("offset") Integer offset, @Param("rows") Integer rows);

    int updateByPrimaryKeySelective(RedisConfigPO record);

    int updateByPrimaryKey(RedisConfigPO record);
}