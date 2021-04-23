package com.tz.redismanager.config.dao;

import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;

import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 23:17
 **/
public interface IConfigDao {

    int deleteByPrimaryKey(Integer id);

    int deleteLogicByPrimaryKey(ConfigDTO dto);

    int deleteLogicByIds(ConfigDTO dto);

    int insert(ConfigPO record);

    ConfigPO selectByPrimaryKey(Integer id);

    List<ConfigPO> selectListByParam(ConfigQueryParam param);

    List<ConfigPO> selectPageByParam(ConfigPageParam param);

    int count(ConfigPageParam param);

    int updateByPrimaryKey(ConfigPO record);
}
