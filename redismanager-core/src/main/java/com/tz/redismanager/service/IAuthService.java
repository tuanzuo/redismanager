package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p></p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
public interface IAuthService {

    ApiResult<AuthResp> login(LoginVO vo);

    ApiResult<Object> logout(AuthContext authContext);
}
