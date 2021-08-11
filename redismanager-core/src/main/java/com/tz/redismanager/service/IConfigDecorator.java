package com.tz.redismanager.service;

import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.ConfigVO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>配置装饰器接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 20:54
 **/
public interface IConfigDecorator {

    /**
     * 分页查询配置
     *
     * @param param
     * @return
     */
    ApiResult<?> queryPageList(ConfigPageParam param);

    /**
     * 添加配置
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> add(ConfigVO vo, AuthContext authContext);

    /**
     * 更新配置
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> update(ConfigVO vo, AuthContext authContext);

    /**
     * 删除配置
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> del(ConfigVO vo, AuthContext authContext);
}
