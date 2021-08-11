package com.tz.redismanager.config.dao;

import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;

import java.util.List;

/**
 * <p>配置Dao接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 23:17
 **/
public interface IConfigDao {

    /**
     * 通过主键物理删除
     *
     * @param id
     * @return
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * 通过主键逻辑删除
     *
     * @param dto
     * @return
     */
    int deleteLogicByPrimaryKey(ConfigDTO dto);

    /**
     * 通过主键集合批量删除
     *
     * @param dto
     * @return
     */
    int deleteLogicByIds(ConfigDTO dto);

    /**
     * 添加
     *
     * @param record
     * @return
     */
    int insert(ConfigPO record);

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    ConfigPO selectByPrimaryKey(Integer id);

    /**
     * 查询
     *
     * @param param
     * @return
     */
    List<ConfigPO> selectListByParam(ConfigQueryParam param);

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    List<ConfigPO> selectPageByParam(ConfigPageParam param);

    /**
     * 查询数量
     *
     * @param param
     * @return
     */
    int count(ConfigQueryParam param);

    /**
     * 查询分页数量
     *
     * @param param
     * @return
     */
    int countPage(ConfigPageParam param);

    /**
     * 通过主键更新
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(ConfigPO record);
}
