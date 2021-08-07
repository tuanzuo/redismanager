package com.tz.redismanager.security.domain;

import com.tz.redismanager.dao.domain.po.UserPO;
import lombok.Getter;
import lombok.Setter;

/**
 * <p></p>
 *
 * @author tuanzou
 * @version 1.6.0
 * @time 2020-12-23 22:04
 **/
@Setter
@Getter
public class AuthContextToRedis {

    private UserPO userPO;

    private AuthContext authContext;
}
