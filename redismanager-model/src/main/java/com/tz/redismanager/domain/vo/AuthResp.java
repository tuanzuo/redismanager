package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * 权限resp
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Getter
@Setter
public class AuthResp {

    /**
     * token
     */
    private String token;

    /**
     * 用户的角色编码List
     */
    private Set<String> roles = new HashSet<>();

}
