package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.po.UserRoleRelationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户角色关系Mapper
 *
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Mapper
public interface UserRoleRelationPOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserRoleRelationPO record);

    int insertSelective(UserRoleRelationPO record);

    UserRoleRelationPO selectByPrimaryKey(Integer id);

    List<RolePO> selectByUser(@Param("userId") Integer userId);

    int updateByPrimaryKeySelective(UserRoleRelationPO record);

    int updateByPrimaryKey(UserRoleRelationPO record);

}