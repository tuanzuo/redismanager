package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.security.domain.AuthContext;

import java.util.List;

/**
 * <p>角色service接口</p>
 *
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
public interface IRoleService {

    ApiResult<?> add(RoleVO vo, AuthContext authContext);

    ApiResult<?> update(RoleVO vo, AuthContext authContext);

    ApiResult<?> updateStatus(List<Integer> ids, Integer status, AuthContext authContext);

    ApiResult<?> queryList(RolePageParam param);

}
