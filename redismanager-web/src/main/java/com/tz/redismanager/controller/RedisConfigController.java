package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.util.UUIDUtils;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * redis连接配置controller
 *
 * @Since:2019-08-23 22:29:01
 * @Version:1.1.0
 */
@RestController
@RequestMapping("/redis/config")
@Validated
public class RedisConfigController {

    @Autowired
    private IRedisConfigService redisConfigService;

    @RequestMapping("list")
    @MethodLog(logPrefix = "查询Redis连接信息", logInputParams = false, logOutputParams = false)
    @Auth
    @Limiter(name = "查询Redis连接信息请求限流", key = "REDIS_CONFIG_LIST_API", qps = 200)
    public ApiResult<?> list(RedisConfigPageParam param, AuthContext authContext) {
        Map<String, List<RedisConfigPO>> map = new HashMap<>();
        param.setUserName(authContext.getUserName());
        param.setIsSuperAdmin(authContext.getRoles().contains(ConstInterface.ROLE_CODE.SUPER_ADMIN) ? ConstInterface.IS_SUPER_ADMIN.YES : ConstInterface.IS_SUPER_ADMIN.NO);
        map.put("configList", redisConfigService.searchList(param));
        return new ApiResult<>(ResultCode.SUCCESS, map);
    }

    @RequestMapping("add")
    @Auth
    public ApiResult<?> add(@Validated({ValidGroup.AddConnection.class}) @RequestBody RedisConfigVO vo, AuthContext authContext) {
        vo.setId(UUIDUtils.generateId());
        redisConfigService.add(vo, authContext);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @RequestMapping("del/{id}")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> del(@NotEmpty(message = "id不能为空") @PathVariable("id") String id, AuthContext authContext) {
        return redisConfigService.delete(id, authContext);
    }

    @RequestMapping("update")
    @Auth
    public ApiResult<?> update(@Validated({ValidGroup.UpdateConnection.class}) @RequestBody RedisConfigVO vo, AuthContext authContext) {
        return redisConfigService.update(vo, authContext);
    }

}
