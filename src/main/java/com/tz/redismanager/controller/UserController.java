package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.service.IUserService;
import com.tz.redismanager.security.SecurityAuth;
import com.tz.redismanager.security.SecurityAuthContext;
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

    @RequestMapping("current")
    @SecurityAuth
    public ApiResult<?> currentUser(SecurityAuthContext authContext) {
        return userService.currentUser(authContext);
    }

    @RequestMapping("update")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.updateUserInfo.class}) @RequestBody UserVO vo, SecurityAuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.update(vo);
    }

    @RequestMapping("update/status")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.updateUserStatus.class}) @RequestBody UserVO vo, SecurityAuthContext authContext) {
        return userService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    /**
     * 登录用户修改自己的密码
     */
    @RequestMapping("update/pwd")
    @SecurityAuth()
    public ApiResult<?> updatePwd(@Validated({ValidGroup.updateUserPwd.class}) @RequestBody UserVO vo, SecurityAuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.updatePwd(vo);
    }

    @RequestMapping("reset/pwd")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> resetPwd(@Validated({ValidGroup.resetUserPwd.class}) @RequestBody UserVO vo, SecurityAuthContext authContext) {
        return userService.resetPwd(vo, authContext);
    }

    @RequestMapping("grant/role")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> grantRole(@Validated({ValidGroup.grantUserRole.class}) @RequestBody UserVO vo, SecurityAuthContext authContext) {
        return userService.grantRole(vo, authContext);
    }

    @RequestMapping("list")
    @MethodLog(logInputParams = false, logOutputParams = false)
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> list(UserPageParam param) {
        return userService.queryList(param);
    }

}
