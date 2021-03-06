package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.po.RolePO;
import com.tz.redismanager.dao.domain.po.UserRoleRelationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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

    int delByIds(@Param("ids") List<Integer> ids, @Param("updater") String updater, @Param("updateTime") Date updateTime, @Param("ifDel") Integer ifDel);

    int insert(UserRoleRelationPO record);

    int insertBatch(@Param("userRoles") List<UserRoleRelationPO> userRoles);

    int insertSelective(UserRoleRelationPO record);

    UserRoleRelationPO selectByPrimaryKey(Integer id);

    List<RolePO> selectByUserRole(@Param("userId") Integer userId, @Param("roleStatus") Integer roleStatus);

    List<UserRoleRelationPO> selectByUserRoleRelation(@Param("userId") Integer userId);

    int updateByPrimaryKeySelective(UserRoleRelationPO record);

    int updateByPrimaryKey(UserRoleRelationPO record);

}