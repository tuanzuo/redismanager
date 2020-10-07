package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.token.TokenAuth;
import com.tz.redismanager.token.TokenContext;
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
    @TokenAuth
    public ApiResult<?> add(@Validated({ValidGroup.addRole.class}) @RequestBody RoleVO vo, TokenContext tokenContext) {
        return roleService.add(vo, tokenContext);
    }

    @RequestMapping("update")
    @TokenAuth
    public ApiResult<?> update(@Validated({ValidGroup.updateRole.class}) @RequestBody RoleVO vo, TokenContext tokenContext) {
        return roleService.update(vo, tokenContext);
    }

    @RequestMapping("update/status")
    @TokenAuth
    public ApiResult<?> updateStatus(@Validated({ValidGroup.updateRoleStatus.class}) @RequestBody RoleVO vo, TokenContext tokenContext) {
        return roleService.updateStatus(vo.getIds(), vo.getStatus(), tokenContext);
    }

    @RequestMapping("list")
    @MethodLog(logInputParams = false, logOutputParams = false)
    @TokenAuth
    public ApiResult<?> list(RolePageParam param) {
        return roleService.queryList(param);
    }

}
