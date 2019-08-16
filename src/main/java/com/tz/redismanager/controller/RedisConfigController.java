package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodExecTime;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.service.IRedisConfigService;
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

@RestController
@RequestMapping("/redis/config")
@Validated
public class RedisConfigController {

    @Autowired
    private IRedisConfigService redisConfigService;

    @RequestMapping("list")
    @MethodExecTime(logInputParams = false, logOutputParams = false)
    public Object list(String searchKey) {
        Map<String, List<RedisConfigPO>> map = new HashMap<>();
        map.put("configList", redisConfigService.searchList(searchKey));
        return map;
    }

    @RequestMapping("add")
    public void add(@Validated({ValidGroup.AddConnection.class}) @RequestBody RedisConfigVO vo) {
        redisConfigService.add(vo);
    }

    @RequestMapping("del/{id}")
    public void del(@NotEmpty(message = "id不能为空") @PathVariable("id") String id) {
        redisConfigService.delete(id);
    }

    @RequestMapping("update")
    public void update(@Validated({ValidGroup.UpdateConnection.class}) @RequestBody RedisConfigVO vo) {
        redisConfigService.update(vo);
    }

}
