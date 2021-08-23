package com.tz.redismanager.security.auth;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.tz.redismanager.security.domain.AuthContext;

/**
 * <p>安全认证上下文Hodler</p>
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-30 19:17
 **/
public class AuthContextHolder {

    private static final ThreadLocal<AuthContext> TOKEN_CONTEXT_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static AuthContext get() {
        return TOKEN_CONTEXT_THREAD_LOCAL.get();
    }

    public static void set(AuthContext authContext) {
        TOKEN_CONTEXT_THREAD_LOCAL.set(authContext);
    }

    public static void remove() {
        TOKEN_CONTEXT_THREAD_LOCAL.remove();
    }
}
