package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.token.TokenContext;

import java.util.List;

/**
 * <p>角色service接口</p>
 *
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
public interface IRoleService {

    ApiResult<?> add(RoleVO vo, TokenContext tokenContext);

    ApiResult<?> update(RoleVO vo, TokenContext tokenContext);

    ApiResult<?> updateStatus(List<Integer> ids, Integer status, TokenContext tokenContext);

    ApiResult<?> queryList(RolePageParam param);

}
