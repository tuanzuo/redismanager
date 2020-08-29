package com.tz.redismanager.domain.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限resp
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
public class AuthResp {

    /**
     * token
     */
    private String token;

    /**
     * 用户信息
     */
    private UserVO user = new UserVO();

    /**
     * 用户的角色List
     */
    private Set<String> rules = new HashSet<>();

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public Set<String> getRules() {
        return rules;
    }

    public void setRules(Set<String> rules) {
        this.rules = rules;
    }
}
