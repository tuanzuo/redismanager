package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.security.SecurityAuth;
import com.tz.redismanager.security.SecurityAuthContext;
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
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> add(@Validated({ValidGroup.addRole.class}) @RequestBody RoleVO vo, SecurityAuthContext authContext) {
        return roleService.add(vo, authContext);
    }

    @RequestMapping("update")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.updateRole.class}) @RequestBody RoleVO vo, SecurityAuthContext authContext) {
        return roleService.update(vo, authContext);
    }

    @RequestMapping("update/status")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.updateRoleStatus.class}) @RequestBody RoleVO vo, SecurityAuthContext authContext) {
        return roleService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    @RequestMapping("list")
    @MethodLog(logInputParams = false, logOutputParams = false)
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> list(RolePageParam param) {
        return roleService.queryList(param);
    }

}
