package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.token.TokenAuth;
import com.tz.redismanager.token.TokenContext;
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
    @TokenAuth
    public Object list(RedisConfigPageParam param) {
        Map<String, List<RedisConfigPO>> map = new HashMap<>();
        map.put("configList", redisConfigService.searchList(param));
        return map;
    }

    @RequestMapping("add")
    @TokenAuth
    public void add(@Validated({ValidGroup.AddConnection.class}) @RequestBody RedisConfigVO vo, TokenContext tokenContext) {
        redisConfigService.add(vo, tokenContext);
    }

    @RequestMapping("del/{id}")
    @TokenAuth
    public void del(@NotEmpty(message = "id不能为空") @PathVariable("id") String id, TokenContext tokenContext) {
        redisConfigService.delete(id, tokenContext);
    }

    @RequestMapping("update")
    @TokenAuth
    public void update(@Validated({ValidGroup.UpdateConnection.class}) @RequestBody RedisConfigVO vo, TokenContext tokenContext) {
        redisConfigService.update(vo, tokenContext);
    }

}
