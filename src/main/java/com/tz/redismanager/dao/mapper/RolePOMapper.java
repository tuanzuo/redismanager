package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.domain.po.RolePO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色Mapper
 *
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Mapper
public interface RolePOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(RolePO record);

    int insertSelective(RolePO record);

    RolePO selectByPrimaryKey(Integer id);

    List<RolePO> getAll();

    int updateByPrimaryKeySelective(RolePO record);

    int updateByPrimaryKey(RolePO record);

}