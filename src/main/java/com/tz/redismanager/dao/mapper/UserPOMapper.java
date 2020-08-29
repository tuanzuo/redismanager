package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.domain.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper
 *
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Mapper
public interface UserPOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(UserPO record);

    int insertSelective(UserPO record);

    UserPO selectByPrimaryKey(Integer id);

    UserPO selectByNamePwd(@Param("name") String name, @Param("pwd") String pwd);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);
}