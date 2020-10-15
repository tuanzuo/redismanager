package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.Auth;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
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
    @Auth
    public ApiResult<?> initContext(@NotEmpty(message = "id不能为空") @PathVariable("id") String id) {
        redisContextService.initContext(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @RequestMapping("context/cache/clear/{id}")
    @Auth
    public ApiResult<?> clearCacheRedisTemplate(@NotEmpty(message = "id不能为空") @PathVariable("id") String id) {
        redisContextService.removeRedisTemplate(id);
        redisContextService.getRedisConfigCache().invalidate(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @RequestMapping("context/test/connection")
    @Auth
    public ApiResult<?> testRedisConnection(@Validated({ValidGroup.TestConnection.class}) @RequestBody RedisConfigVO vo) {
        return redisContextService.testRedisConnection(vo);
    }

    @RequestMapping("key/list")
    @Auth
    @MethodLog(logPrefix = "查询Redis的Key接口", logInputParams = false, logOutputParams = false)
    public ApiResult<?> keyList(@NotEmpty(message = "id不能为空") String id, @NotEmpty(message = "查询条件不能为空") String searchKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyList", redisAdminService.searchKey(id, searchKey));
        return new ApiResult<>(ResultCode.SUCCESS, map);
    }

    @RequestMapping("key/value")
    @Auth
    @MethodLog(logPrefix = "查询Redis的Key对应value接口", logInputParams = false, logOutputParams = false)
    public ApiResult<?> keyValue(@Validated @RequestBody RedisValueQueryVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyValue", redisAdminService.searchKeyValue(vo));
        return new ApiResult<>(ResultCode.SUCCESS, map);
    }

    @RequestMapping("key/del")
    @Auth
    public ApiResult<?> delKeys(@Validated @RequestBody RedisKeyDelVO vo) {
        return redisAdminService.delKeys(vo);
    }

    @RequestMapping("key/rename")
    @Auth
    public ApiResult<?> renameKey(@Validated({ValidGroup.RenameKey.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.renameKey(vo);
    }

    @RequestMapping("key/setTtl")
    @Auth
    public ApiResult<?> setTtl(@Validated({ValidGroup.SetTTL.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.setTtl(vo);
    }

    @RequestMapping("key/updateValue")
    @Auth
    public ApiResult<?> updateValue(@Validated({ValidGroup.UpdateKeyValue.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.updateValue(vo);
    }

    @RequestMapping("key/addKey")
    @Auth
    public ApiResult<?> addKey(@Validated @RequestBody RedisKeyAddVO vo) {
        return redisAdminService.addKey(vo);
    }

}
