package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.UserListResp;
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
 * 用户管理
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-30 18:30
 **/
@RestController
@RequestMapping("/auth/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 注册用户接口
     * @param vo
     * @return
     */
    @RequestMapping("register")
    @Limiter(name = "注册用户接口限流", key = "REGISTER_USER_API", qps = 1)
    public ApiResult<?> register(@Validated({ValidGroup.AddUserInfo.class}) @RequestBody UserVO vo) {
        return userService.register(vo);
    }

    /**
     * 在线用户数接口
     * @return
     */
    @RequestMapping("count/online")
    @Auth
    public ApiResult<Long> countOnline() {
        return userService.countOnline();
    }

    /**
     * 当前用户信息接口
     * @param authContext
     * @return
     */
    @RequestMapping("current")
    @Auth
    public ApiResult<?> currentUser(AuthContext authContext) {
        return ApiResult.buildSuccess(userService.currentUser(authContext));
    }

    /**
     * 修改用户信息接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.UpdateUserInfo.class}) @RequestBody UserVO vo, AuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.update(vo);
    }

    /**
     * 修改用户状态接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update/status")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.UpdateUserStatus.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    /**
     * 个人页-个人设置-修改密码接口
     */
    @RequestMapping("update/pwd")
    @Auth()
    public ApiResult<?> updatePwd(@Validated({ValidGroup.UpdateUserPwd.class}) @RequestBody UserVO vo, AuthContext authContext) {
        vo.setId(authContext.getUserId());
        return userService.updatePwd(vo);
    }

    /**
     * 用户管理-用户列表-重置密码接口
     */
    @RequestMapping("reset/pwd")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> resetPwd(@Validated({ValidGroup.ResetUserPwd.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.resetPwd(vo, authContext);
    }

    /**
     * 用户授权接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("grant/role")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> grantRole(@Validated({ValidGroup.GrantUserRole.class}) @RequestBody UserVO vo, AuthContext authContext) {
        return userService.grantRole(vo, authContext);
    }

    /**
     * 查询用户列表接口
     * @param param
     * @return
     */
    @RequestMapping("list")
    @MethodLog(logPrefix = "查询用户列表接口", logInputParams = false, logOutputParams = false)
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    @Limiter(name = "查询用户列表接口限流", key = "USER_LIST_API", qps = 200)
    public ApiResult<UserListResp> list(UserPageParam param) {
        return userService.queryList(param);
    }

}
