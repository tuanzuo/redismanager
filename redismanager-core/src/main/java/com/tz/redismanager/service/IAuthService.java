package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>授权服务接口</p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
public interface IAuthService {

    /**
     * 登录
     *
     * @param vo
     * @return
     */
    ApiResult<AuthResp> login(LoginVO vo);

    /**
     * 退出
     *
     * @param authContext
     * @return
     */
    ApiResult<Object> logout(AuthContext authContext);
}
