package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * redis操作管理
 *
 * @author tuanzuo
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
    @Autowired
    private IRedisConfigService redisConfigService;
    @Autowired
    private IStatisticService statisticService;

    /**
     * 初始化redis连接接口
     * @param id
     * @return
     */
    @RequestMapping("context/init/{id}")
    @Auth
    public ApiResult<?> initContext(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        redisContextService.initContext(id);
        statisticService.addRedisConfigVisit(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    /**
     * 清理redis连接缓存接口
     * @param id
     * @return
     */
    @RequestMapping("context/cache/clear/{id}")
    @Auth
    public ApiResult<?> clearCacheRedisTemplate(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        redisContextService.removeRedisTemplate(id);
        redisConfigService.invalidateCache(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    /**
     * 测试redis连接接口
     * @param vo
     * @return
     */
    @RequestMapping("context/test/connection")
    @Auth
    public ApiResult<String> testRedisConnection(@Validated({ValidGroup.TestConnection.class}) @RequestBody RedisConfigVO vo) {
        return redisContextService.testRedisConnection(vo);
    }

    /**
     * 查询redis服务信息接口
     * @param id
     * @return
     */
    @RequestMapping("server/info/{id}")
    @Auth
    public ApiResult<RedisServerInfoVO> queryServerInfo(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return redisAdminService.queryServerInfo(id);
    }

    /**
     * 查询redis的key列表接口
     * @param id
     * @param searchKey
     * @return
     */
    @RequestMapping("key/list")
    @Auth
    @MethodLog(logPrefix = "查询Redis的Key列表接口", logInputParams = false, logOutputParams = false)
    @Limiter(name = "查询Redis的Key列表接口请求限流", key = "REDIS_ADMIN_KEY_LIST_API", qps = 200)
    public ApiResult<Map<String, Object>> keyList(@NotNull(message = "id不能为空") Long id, @NotEmpty(message = "查询条件不能为空") String searchKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyList", redisAdminService.searchKey(id, searchKey));
        return new ApiResult<>(ResultCode.SUCCESS, map);
    }

    /**
     * 查询redis的key对应value接口
     * @param vo
     * @return
     */
    @RequestMapping("key/value")
    @Auth
    @MethodLog(logPrefix = "查询Redis的Key对应value接口", logInputParams = false, logOutputParams = false)
    @Limiter(name = "查询Redis的Key对应value接口限流", key = "REDIS_ADMIN_KEY_VALUE_API", qps = 200)
    public ApiResult<Map<String, Object>> keyValue(@Validated @RequestBody RedisValueQueryVO vo) {
        Map<String, Object> map = new HashMap<>();
        map.put("keyValue", redisAdminService.searchKeyValue(vo));
        return new ApiResult<>(ResultCode.SUCCESS, map);
    }

    /**
     * 删除redis的key接口
     * @param vo
     * @return
     */
    @RequestMapping("key/del")
    @Auth
    public ApiResult<?> delKeys(@Validated @RequestBody RedisKeyDelVO vo) {
        return redisAdminService.delKeys(vo);
    }

    /**
     * 修改redis的key名称接口
     * @param vo
     * @return
     */
    @RequestMapping("key/rename")
    @Auth
    public ApiResult<?> renameKey(@Validated({ValidGroup.RenameKey.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.renameKey(vo);
    }

    /**
     * 设置redis的key过期时间接口
     * @param vo
     * @return
     */
    @RequestMapping("key/setTtl")
    @Auth
    public ApiResult<?> setTtl(@Validated({ValidGroup.SetTTL.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.setTtl(vo);
    }

    /**
     * 修改redis的key对应的value接口
     * @param vo
     * @return
     */
    @RequestMapping("key/updateValue")
    @Auth
    public ApiResult<?> updateValue(@Validated({ValidGroup.UpdateKeyValue.class}) @RequestBody RedisKeyUpdateVO vo) {
        return redisAdminService.updateValue(vo);
    }

    /**
     * 添加redis的key接口
     * @param vo
     * @return
     */
    @RequestMapping("key/addKey")
    @Auth
    public ApiResult<?> addKey(@Validated @RequestBody RedisKeyAddVO vo) {
        return redisAdminService.addKey(vo);
    }

}
