package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.RoleDTO;
import com.tz.redismanager.dao.domain.po.UserRoleRelationPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 用户角色关系Mapper
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Mapper
public interface UserRoleRelationPOMapper {

    int deleteByPrimaryKey(Long id);

    int delByIds(@Param("ids") List<Long> ids, @Param("updater") String updater, @Param("updateTime") Date updateTime, @Param("ifDel") Integer ifDel);

    int insert(UserRoleRelationPO record);

    int insertBatch(@Param("userRoles") List<UserRoleRelationPO> userRoles);

    int insertSelective(UserRoleRelationPO record);

    UserRoleRelationPO selectByPrimaryKey(Long id);

    List<RoleDTO> selectByUserRole(@Param("userId") Long userId, @Param("userIds") Set<Long> userIds,
                                   @Param("roleStatus") Integer roleStatus, @Param("ifDel") Integer ifDel);

    List<UserRoleRelationPO> selectByUserRoleRelation(@Param("userId") Long userId, @Param("ifDel") Integer ifDel);

    int updateByPrimaryKeySelective(UserRoleRelationPO record);

    int updateByPrimaryKey(UserRoleRelationPO record);

}
