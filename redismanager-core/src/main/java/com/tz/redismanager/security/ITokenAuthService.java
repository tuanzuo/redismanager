package com.tz.redismanager.security;

import com.tz.redismanager.dao.domain.po.UserPO;

/**
 * <p>Token认证服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:38
 **/
public interface ITokenAuthService {

    String handleLogin(UserPO userPO, AuthContext context);

    void handleLogout(AuthContext context);

    AuthContext getAuthContext(String token);
}
