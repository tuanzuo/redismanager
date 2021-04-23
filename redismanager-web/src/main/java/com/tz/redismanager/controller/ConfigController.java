package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.ConfigVO;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IConfigDecorator;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>配置controller</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 20:49
 **/
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private IConfigDecorator configDecorator;

    @RequestMapping("list")
    @MethodLog(logPrefix = "查询配置列表", logInputParams = false, logOutputParams = false)
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    @Limiter(name = "查询配置列表请求限流", key = "CONFIG_LIST_API", qps = 200)
    public ApiResult<?> list(ConfigPageParam param) {
        return configDecorator.queryList(param);
    }

    @RequestMapping("add")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> add(@Validated({ValidGroup.AddConfig.class}) @RequestBody ConfigVO vo, AuthContext authContext) {
        return configDecorator.add(vo, authContext);
    }

    @RequestMapping("update")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> update(@Validated({ValidGroup.UpdateConfig.class}) @RequestBody ConfigVO vo, AuthContext authContext) {
        return configDecorator.update(vo, authContext);
    }

    @RequestMapping("del")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> del(@Validated({ValidGroup.DelConfig.class}) @RequestBody ConfigVO vo, AuthContext authContext) {
        return configDecorator.del(vo, authContext);
    }

}
