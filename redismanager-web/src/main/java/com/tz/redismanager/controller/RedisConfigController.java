package com.tz.redismanager.controller;

import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.vo.RedisConfigPageVO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.validator.ValidGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

/**
 * redis连接配置管理
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:29:01
 * @Version:1.1.0
 */
@RestController
@RequestMapping("/redis/config")
@Validated
public class RedisConfigController {

    @Autowired
    private IRedisConfigService redisConfigService;

    /**
     * 查询redis连接信息列表接口
     * @param param
     * @param authContext
     * @return
     */
    @RequestMapping("list")
    @MethodLog(logPrefix = "查询Redis连接信息列表接口", logInputParams = false, logOutputParams = false)
    @Auth
    @Limiter(name = "查询Redis连接信息列表接口限流", key = "REDIS_CONFIG_LIST_API", qps = 200)
    public ApiResult<RedisConfigPageVO> list(RedisConfigPageParam param, AuthContext authContext) {
        param.setUserName(authContext.getUserName());
        param.setIsSuperAdmin(authContext.getRoles().contains(ConstInterface.ROLE_CODE.SUPER_ADMIN) ? ConstInterface.IS_SUPER_ADMIN.YES : ConstInterface.IS_SUPER_ADMIN.NO);
        return redisConfigService.searchList(param);
    }

    /**
     * 添加redis连接信息接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("add")
    @Auth
    public ApiResult<?> add(@Validated({ValidGroup.AddConnection.class}) @RequestBody RedisConfigVO vo, AuthContext authContext) {
        redisConfigService.add(vo, authContext);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    /**
     * 删除redis连接信息接口
     * @param id
     * @param authContext
     * @return
     */
    @RequestMapping("del/{id}")
    @Auth(permitRoles = {ConstInterface.ROLE_CODE.SUPER_ADMIN})
    public ApiResult<?> del(@NotNull(message = "id不能为空") @PathVariable("id") Long id, AuthContext authContext) {
        return redisConfigService.delete(id, authContext);
    }

    /**
     * 修改redis连接信息接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update")
    @Auth
    public ApiResult<?> update(@Validated({ValidGroup.UpdateConnection.class}) @RequestBody RedisConfigVO vo, AuthContext authContext) {
        return redisConfigService.update(vo, authContext);
    }

    /**
     * 上传文件接口
     * @param file
     * @param authContext
     * @return
     */
    @RequestMapping("/upload")
    @Auth
    public ApiResult<?> upload(@RequestParam("file") MultipartFile file, AuthContext authContext) {
        return redisConfigService.upload(file, authContext);
    }

    /**
     * 下载文件接口
     * @param fileName
     * @param authContext
     * @return
     */
    @RequestMapping("/download")
    @Auth
    public ResponseEntity<byte[]> download(@RequestParam("fileName") String fileName, AuthContext authContext) {
        return redisConfigService.download(fileName, authContext);
    }


}
