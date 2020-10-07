package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.token.TokenContext;

import java.util.List;

/**
 * <p>用户service接口</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
public interface IUserService {

    ApiResult<?> register(UserVO vo);

    ApiResult<?> currentUser(TokenContext tokenContext);

    ApiResult<?> update(UserVO vo);

    ApiResult<?> updateStatus(List<Integer> ids, Integer status, TokenContext tokenContext);

    ApiResult<?> updatePwd(UserVO vo);

    ApiResult<?> resetPwd(UserVO vo, TokenContext tokenContext);

    ApiResult<?> queryList(UserPageParam param);
}
