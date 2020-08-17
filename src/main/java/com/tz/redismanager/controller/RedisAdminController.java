package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.validator.ValidGroup;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * redis操作controller
 *
 * @Since:2019-08-23 22:28:29
 * @Version:1.1.0
 */
@RestController
@RequestMapping("/redis/admin")
@Validated
public class RedisAdminController {

    @Autowired
    private IRedisAdminService redisAdminService;
    @Autowired
    private IRedisContextService redisContextService;

    @RequestMapping("context/init/{id}")
    public void initContext(@NotEmpty(message = "id不能为空") @PathVariable("id") String id) {
        redisContextService.initContext(id);
    }

    @RequestMapping("context/cache/clear/{id}")
    public void clearCacheRedisTemplate(@NotEmpty(message = "id不能为空") @PathVariable("id") String id) {
        redisContextService.removeRedisTemplate(id);
        redisContextService.getRedisConfigCache().invalidate(id);
    }

    @RequestMapping("context/test/connection")
    public ApiResult<String> testRedisConnection(@Validated({ValidGroup.TestConnection.class}) @RequestBody RedisConfigVO vo) {
        return redisContextService.testRedisConnection(vo);
    }

    @RequestMapping("key/list")
    @MethodLog(logPrefix = "查询Redis的Key接口", logInputParams = false, logOutputParams = false)
    public Object keyList(@NotEmpty(message = "id不能为空") String id, @NotEmpty(message = "查询条件不能为空") String searchKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyList", redisAdminService.searchKey(id, searchKey));
        return map;
    }

    @RequestMapping("key/value")
    @MethodLog(logPrefix = "查询Redis的Key对应value接口", logInputParams = false, logOutputParams = false)
    public Object keyValue(@Validated @RequestBody RedisValueQueryVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyValue", redisAdminService.searchKeyValue(vo));
        return map;
    }

    @RequestMapping("key/del")
    public void delKeys(@Validated @RequestBody RedisKeyDelVO vo) {
        redisAdminService.delKeys(vo);
    }

    @RequestMapping("key/rename")
    public void renameKey(@Validated({ValidGroup.RenameKey.class}) @RequestBody RedisKeyUpdateVO vo) {
        redisAdminService.renameKey(vo);
    }

    @RequestMapping("key/setTtl")
    public void setTtl(@Validated({ValidGroup.SetTTL.class}) @RequestBody RedisKeyUpdateVO vo) {
        redisAdminService.setTtl(vo);
    }

    @RequestMapping("key/updateValue")
    public void updateValue(@Validated({ValidGroup.UpdateKeyValue.class}) @RequestBody RedisKeyUpdateVO vo) {
        redisAdminService.updateValue(vo);
    }

    @RequestMapping("key/addKey")
    public ApiResult<?> addKey(@Validated @RequestBody RedisKeyAddVO vo) {
        return redisAdminService.addKey(vo);
    }

}
