package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.dao.domain.po.UserPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    UserPO selectByName(@Param("name") String name);

    UserPO selectByNamePwd(@Param("name") String name, @Param("pwd") String pwd, @Param("ifDel") Integer ifDel);

    List<UserPO> selectPage(@Param("name") String name, @Param("status") Integer status, @Param("offset") Integer offset,
                            @Param("rows") Integer rows, @Param("ifDel") Integer ifDel);

    List<UserAnalysisDTO> selectToAnalysis(@Param("ifDel") Integer ifDel);

    int countUser(@Param("name") String name, @Param("status") Integer status, @Param("ifDel") Integer ifDel);

    int updateByPrimaryKeySelective(UserPO record);

    int updateByPrimaryKey(UserPO record);

    int updateByPwd(@Param("id") Integer id, @Param("pwd") String pwd, @Param("oldPwd") String oldPwd);

    int batchUpdateStatus(@Param("ids") List<Integer> ids, @Param("status") Integer status, @Param("updater") String updater);
}