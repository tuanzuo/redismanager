package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.service.IUserService;
import com.tz.redismanager.token.TokenAuth;
import com.tz.redismanager.token.TokenContext;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户controller
 *
 * @version 1.3.0
 * @time 2020-08-30 18:30
 **/
@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("register")
    public ApiResult<?> register(@Validated({ValidGroup.addUserInfo.class}) @RequestBody UserVO vo) {
        return userService.register(vo);
    }

    @RequestMapping("update/info")
    @TokenAuth
    public ApiResult<?> updateInfo(@Validated({ValidGroup.updateUserInfo.class}) @RequestBody UserVO vo, TokenContext tokenContext) {
        vo.setOldName(tokenContext.getUserName());
        return userService.updateInfo(vo);
    }

    @RequestMapping("update/pwd")
    @TokenAuth
    public ApiResult<?> updatePwd(@Validated({ValidGroup.updateUserPwd.class}) @RequestBody UserVO vo, TokenContext tokenContext) {
        vo.setName(tokenContext.getUserName());
        return userService.updatePwd(vo);
    }

    @RequestMapping("list")
    @MethodLog(logInputParams = false, logOutputParams = false)
    @TokenAuth
    public Object list(String name, Integer currentPage, Integer pageSize) {
        return userService.queryList(name, currentPage, pageSize);
    }

}