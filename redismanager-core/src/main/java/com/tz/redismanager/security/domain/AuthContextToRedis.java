package com.tz.redismanager.security.domain;

import com.tz.redismanager.dao.domain.po.UserPO;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>Redis中的AuthContext</p>
 *
 * @author tuanzou
 * @version 1.6.0
 * @time 2020-12-23 22:04
 **/
@Setter
@Getter
public class AuthContextToRedis {

    /**
     * 用户PO
     */
    private UserPO userPO;

    /**
     * 安全认证上下文
     */
    private AuthContext authContext;
}
