package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.security.SecurityAuth;
import com.tz.redismanager.security.SecurityAuthContext;
import com.tz.redismanager.validator.ValidGroup;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @MethodLog(logInputParams = false, logOutputParams = false)
    @SecurityAuth
    public Object list(RedisConfigPageParam param) {
        Map<String, List<RedisConfigPO>> map = new HashMap<>();
        map.put("configList", redisConfigService.searchList(param));
        return map;
    }

    @RequestMapping("add")
    @SecurityAuth
    public void add(@Validated({ValidGroup.AddConnection.class}) @RequestBody RedisConfigVO vo, SecurityAuthContext authContext) {
        redisConfigService.add(vo, authContext);
    }

    @RequestMapping("del/{id}")
    @SecurityAuth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public void del(@NotEmpty(message = "id不能为空") @PathVariable("id") String id, SecurityAuthContext authContext) {
        redisConfigService.delete(id, authContext);
    }

    @RequestMapping("update")
    @SecurityAuth
    public void update(@Validated({ValidGroup.UpdateConnection.class}) @RequestBody RedisConfigVO vo, SecurityAuthContext authContext) {
        redisConfigService.update(vo, authContext);
    }

}
