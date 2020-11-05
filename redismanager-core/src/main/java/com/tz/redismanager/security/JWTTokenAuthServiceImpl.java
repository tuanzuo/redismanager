package com.tz.redismanager.security;

import com.tz.redismanager.dao.domain.po.UserPO;

/**
 * <p>JWT实现Token认证</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:39
 **/
public class JWTTokenAuthServiceImpl implements ITokenAuthService {

    @Override
    public String handleLogin(UserPO userPO, AuthContext context) {
        return null;
    }

    @Override
    public void handleLogout(AuthContext context) {

    }

    @Override
    public AuthContext getAuthContext(String token) {
        return null;
    }
}
