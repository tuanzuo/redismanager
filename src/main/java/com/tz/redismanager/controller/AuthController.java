package com.tz.redismanager.controller;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限controller
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@RestController
@RequestMapping("/")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @RequestMapping("login")
    public ApiResult<?> login(@RequestBody LoginVO vo) {
        return authService.login(vo);
    }

    @RequestMapping("logout")
    public ApiResult<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(ConstInterface.Auth.AUTHORIZATION);
        return authService.logout(token);
    }

}
