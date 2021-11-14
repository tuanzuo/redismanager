package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.PostmanConfigVO;
import com.tz.redismanager.domain.vo.RequestConfigVO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>Postman配置服务接口</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 21:10
 **/
public interface IPostmanConfigService {

    /**
     * 添加
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> add(PostmanConfigVO vo, AuthContext authContext);

    /**
     * 修改
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> update(PostmanConfigVO vo, AuthContext authContext);

    /**
     * 删除
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> del(PostmanConfigVO vo, AuthContext authContext);

    /**
     * 查询列表
     *
     * @param vo
     * @return
     */
    ApiResult<?> queryList(PostmanConfigVO vo, AuthContext authContext);

    /**
     * 请求
     * @param vo
     * @param authContext
     * @return
     */
    Object request(RequestConfigVO vo, AuthContext authContext);
}
