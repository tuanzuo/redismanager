package com.tz.redismanager.token;

import java.util.HashSet;
import java.util.Set;

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
    private String toToken;
    /**
     * 用户的角色编码List
     */
    private Set<String> roles = new HashSet<>();

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

    public String getToToken() {
        return toToken;
    }

    public void setToToken(String toToken) {
        this.toToken = toToken;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
