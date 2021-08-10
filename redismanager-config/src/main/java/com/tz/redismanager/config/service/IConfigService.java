package com.tz.redismanager.config.service;

import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;

import java.util.List;

/**
 * <p>配置服务接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 22:40
 **/
public interface IConfigService {

    /**
     * 分页查询
     *
     * @param param
     * @return
     */
    List<ConfigPO> queryPageList(ConfigPageParam param);

    /**
     * 查询列表
     *
     * @param param
     * @return
     */
    List<ConfigPO> queryList(ConfigQueryParam param);

    /**
     * 查询条数
     *
     * @param param
     * @return
     */
    int count(ConfigQueryParam param);

    /**
     * 查询分页条数
     *
     * @param param
     * @return
     */
    int countPage(ConfigPageParam param);

    /**
     * 添加配置
     *
     * @param configPO
     * @return
     */
    int addConfig(ConfigPO configPO);

    /**
     * 修改配置
     *
     * @param configPO
     * @return
     */
    int updateConfig(ConfigPO configPO);

    /**
     * 删除配置
     *
     * @param dto
     * @return
     */
    int delConfig(ConfigDTO dto);
}
