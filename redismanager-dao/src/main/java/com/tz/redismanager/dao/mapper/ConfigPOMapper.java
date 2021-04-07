package com.tz.redismanager.dao.mapper;

import com.tz.redismanager.dao.domain.po.ConfigPO;

/**
 * <p>配置Mapper</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-02 21:47
 **/
public interface ConfigPOMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(ConfigPO record);

    int insertSelective(ConfigPO record);

    ConfigPO selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigPO record);

    int updateByPrimaryKey(ConfigPO record);
}