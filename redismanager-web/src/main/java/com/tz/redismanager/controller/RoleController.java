package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.PageResp;
import com.tz.redismanager.domain.vo.RoleResp;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 角色管理
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-08-30 18:30
 **/
@RestController
@RequestMapping("/auth/role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    /**
     * 添加角色接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("add")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> add(@Validated({ValidGroup.AddRole.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.add(vo, authContext);
    }

    /**
     * 修改角色接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.UpdateRole.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.update(vo, authContext);
    }

    /**
     * 修改角色状态接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update/status")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> updateStatus(@Validated({ValidGroup.UpdateRoleStatus.class}) @RequestBody RoleVO vo, AuthContext authContext) {
        return roleService.updateStatus(vo.getIds(), vo.getStatus(), authContext);
    }

    /**
     * 查询角色列表接口
     * @param param
     * @return
     */
    @RequestMapping("list")
    @MethodLog(logPrefix = "查询角色列表接口", logInputParams = false, logOutputParams = false)
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    @Limiter(name = "查询角色列表接口限流", key = "ROLE_LIST_API", qps = 200)
    public ApiResult<PageResp<RoleResp>> list(RolePageParam param) {
        return roleService.queryList(param);
    }

}
