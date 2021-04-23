package com.tz.redismanager.service;

import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.ConfigVO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 20:54
 **/
public interface IConfigDecorator {

    ApiResult<?> queryList(ConfigPageParam param);

    ApiResult<?> add(ConfigVO vo, AuthContext authContext);

    ApiResult<?> update(ConfigVO vo, AuthContext authContext);

    ApiResult<?> del(ConfigVO vo, AuthContext authContext);
}
