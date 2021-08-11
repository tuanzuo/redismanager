package com.tz.redismanager.service;

import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>Auth缓存服务</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-09-29 22:12
 **/
public interface IAuthCacheService {

    /**
     * 设置授权信息
     *
     * @param userName
     * @param encodePwd
     * @param expireTime
     * @param authContext
     */
    void setAuthInfo(String userName, String encodePwd, Integer expireTime, AuthContext authContext);

    /**
     * 删除授权信息
     *
     * @param userName
     * @param encodePwd
     */
    void delAuthInfo(String userName, String encodePwd);

    /**
     * 删除授权信息(退出系统使用)
     *
     * @param authContext
     */
    void delAuthInfoToLogout(AuthContext authContext);
}

