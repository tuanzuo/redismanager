package com.tz.redismanager.token;

/**
 * <p>Token上下文</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 18:36
 **/
public class TokenContext {

    private String userName;
    private String token;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
