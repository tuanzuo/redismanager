package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    List<RedisConfigPO> selectByIds(@Param("ids")Set<String> ids);

    List<RedisConfigPO> selectPage(Map<String, Object> params);

    List<RedisConfigAnalysisDTO> selectToAnalysis(@Param("ifDel") Integer ifDel);

    int updateByPrimaryKeySelective(RedisConfigPO record);

    int updateByPrimaryKey(RedisConfigPO record);
}