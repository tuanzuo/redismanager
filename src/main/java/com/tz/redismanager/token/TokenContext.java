package com.tz.redismanager.token;

/**
 * <p>Token上下文</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 18:36
 **/
public class TokenContext {

    private Integer userId;
    private String userName;
    private String token;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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
