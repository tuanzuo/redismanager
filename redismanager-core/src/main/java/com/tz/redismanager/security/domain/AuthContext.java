package com.tz.redismanager.security.domain;


import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>安全认证上下文</p>
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-30 18:36
 **/
@Getter
@Setter
public class AuthContext {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户的角色编码List
     */
    private Set<String> roles = new HashSet<>();

    /**
     * redis的Token使用
     */
    private String token;
    private String toToken;

}
