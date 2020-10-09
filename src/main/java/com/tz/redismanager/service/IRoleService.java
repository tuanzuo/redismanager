package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.security.SecurityAuthContext;

import java.util.List;

/**
 * <p>角色service接口</p>
 *
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
public interface IRoleService {

    ApiResult<?> add(RoleVO vo, SecurityAuthContext authContext);

    ApiResult<?> update(RoleVO vo, SecurityAuthContext authContext);

    ApiResult<?> updateStatus(List<Integer> ids, Integer status, SecurityAuthContext authContext);

    ApiResult<?> queryList(RolePageParam param);

}
