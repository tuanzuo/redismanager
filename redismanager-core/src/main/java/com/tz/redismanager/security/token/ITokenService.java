package com.tz.redismanager.security.token;

import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>Token服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:38
 **/
public interface ITokenService {

    String handleLogin(UserPO userPO, AuthContext context);

    void handleLogout(AuthContext context);

    AuthContext getAuthContext(String token);
}
