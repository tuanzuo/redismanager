package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.RedisConfigExtDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>redis连接配置扩展Mapper</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:19
 **/
@Mapper
public interface RedisConfigExtPOMapper {

    int deleteByPrimaryKey(Long id);

    int insert(RedisConfigExtPO record);

    int insertSelective(RedisConfigExtPO record);

    int insertBatch(@Param("list") List<RedisConfigExtPO> list);

    RedisConfigExtPO selectByPrimaryKey(Long id);

    List<RedisConfigExtPO> selectList(RedisConfigExtDTO record);

    int updateByPrimaryKeySelective(RedisConfigExtPO record);

    int updateByRconfigIds(RedisConfigExtDTO record);

    int updateByPrimaryKey(RedisConfigExtPO record);
}