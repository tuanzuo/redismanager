package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色controller
 *
 * @version 1.4.0
 * @time 2020-08-30 18:30
 **/
@RestController
@RequestMapping("/auth/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @RequestMapping("add")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> add(@Validated({ValidGroup.AddRole.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.add(vo, authContext);
    }

    @RequestMapping("update")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.UpdateRole.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.update(vo, authContext);
    }

    @RequestMapping("update/status")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.UpdateRoleStatus.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    @RequestMapping("list")
    @MethodLog(logInputParams = false, logOutputParams = false)
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> list(RolePageParam param) {
        return roleService.queryList(param);
    }

}
