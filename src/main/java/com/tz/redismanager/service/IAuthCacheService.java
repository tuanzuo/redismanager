package com.tz.redismanager.service;

import com.tz.redismanager.security.SecurityAuthContext;

/**
 * <p>Auth缓存服务</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-09-29 22:12
 **/
public interface IAuthCacheService {

    void setAuthInfo(String userName, String encodePwd, SecurityAuthContext authContext);

    void delAuthInfo(String userName, String encodePwd);

    void delAuthInfoToLogout(SecurityAuthContext authContext);
}

