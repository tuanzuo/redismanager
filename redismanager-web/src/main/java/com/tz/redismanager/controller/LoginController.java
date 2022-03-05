package com.tz.redismanager.controller;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.service.IAuthService;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录管理
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private IAuthService authService;

    /**
     * 登录接口
     * @param vo
     * @return
     */
    @RequestMapping("login")
    public ApiResult<AuthResp> login(@Validated({ValidGroup.Login.class}) @RequestBody LoginVO vo) {
        return authService.login(vo);
    }

    /**
     * 登出接口
     * @param authContext
     * @return
     */
    @RequestMapping("logout")
    @Auth(required = false)
    public ApiResult<?> logout(AuthContext authContext) {
        return authService.logout(authContext);
    }

}
