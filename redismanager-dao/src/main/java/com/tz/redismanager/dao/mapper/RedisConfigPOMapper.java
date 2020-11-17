package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.RedisConfigAnalysisDTO;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
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

    List<RedisConfigPO> selectPage(@Param("searchKey") String searchKey, @Param("isPublic") Integer isPublic,
                                   @Param("userName") String userName, @Param("isSuperAdmin") Integer isSuperAdmin,
                                   @Param("offset") Integer offset, @Param("rows") Integer rows);

    List<RedisConfigAnalysisDTO> selectToAnalysis();

    int updateByPrimaryKeySelective(RedisConfigPO record);

    int updateByPrimaryKey(RedisConfigPO record);
}