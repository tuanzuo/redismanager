package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IUserService;
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
    public ApiResult<?> register(@Validated({ValidGroup.AddUserInfo.class}) @RequestBody UserVO vo) {
        return userService.register(vo);
    }

    @RequestMapping("count/online")
    @Auth
    public ApiResult<?> countOnline() {
        return userService.countOnline();
    }

    @RequestMapping("current")
    @Auth
    public ApiResult<?> currentUser(AuthContext authContext) {
        return ApiResult.buildSuccess(userService.currentUser(authContext));
    }

    @RequestMapping("update")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.UpdateUserInfo.class}) @RequestBody UserVO vo, AuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.update(vo);
    }

    @RequestMapping("update/status")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.UpdateUserStatus.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    /**
     * 个人页-个人设置-修改密码
     */
    @RequestMapping("update/pwd")
    @Auth()
    public ApiResult<?> updatePwd(@Validated({ValidGroup.UpdateUserPwd.class}) @RequestBody UserVO vo, AuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.updatePwd(vo);
    }

    /**
     * 用户管理-用户列表-重置密码
     */
    @RequestMapping("reset/pwd")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> resetPwd(@Validated({ValidGroup.ResetUserPwd.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.resetPwd(vo, authContext);
    }

    @RequestMapping("grant/role")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> grantRole(@Validated({ValidGroup.GrantUserRole.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.grantRole(vo, authContext);
    }

    @RequestMapping("list")
    @MethodLog(logPrefix = "查询用户列表", logInputParams = false, logOutputParams = false)
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    @Limiter(name = "查询用户列表请求限流", key = "USER_LIST_API", qps = 200)
    public ApiResult<?> list(UserPageParam param) {
        return userService.queryList(param);
    }

}
