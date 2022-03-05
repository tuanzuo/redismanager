package com.tz.redismanager.controller;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.PostmanConfigResp;
import com.tz.redismanager.domain.vo.PostmanConfigVO;
import com.tz.redismanager.domain.vo.RequestConfigVO;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IPostmanConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Postman配置管理
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 21:10
 **/
@RestController
@RequestMapping("/auth/postman/config")
public class PostmanConfigController {

    @Autowired
    private IPostmanConfigService postmanConfigService;

    /**
     * 添加接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("add")
    @Auth
    public ApiResult<?> add(@RequestBody PostmanConfigVO vo, AuthContext authContext) {
        return postmanConfigService.add(vo, authContext);
    }

    /**
     * 修改接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("update")
    @Auth
    public ApiResult<?> update(@RequestBody PostmanConfigVO vo, AuthContext authContext) {
        return postmanConfigService.update(vo, authContext);
    }

    /**
     * 删除接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("del")
    @Auth
    public ApiResult<?> del(@RequestBody PostmanConfigVO vo, AuthContext authContext) {
        return postmanConfigService.del(vo, authContext);
    }

    /**
     * 分享接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("share")
    @Auth
    public ApiResult<?> share(@RequestBody PostmanConfigVO vo, AuthContext authContext) {
        return postmanConfigService.share(vo, authContext);
    }

    /**
     * 查询列表接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("list")
    @Auth
    public ApiResult<List<PostmanConfigResp>> list(PostmanConfigVO vo, AuthContext authContext) {
        return postmanConfigService.queryList(vo, authContext);
    }

    /**
     * 执行服务端请求接口
     * @param vo
     * @param authContext
     * @return
     */
    @RequestMapping("request")
    @Auth
    @Limiter(name = "服务端执行请求", key = "POSTMAN_CONFIG_REQUEST_API", qps = 100)
    public Object request(@RequestBody RequestConfigVO vo, AuthContext authContext) {
        return postmanConfigService.request(vo, authContext);
    }

}
