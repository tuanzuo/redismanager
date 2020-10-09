package com.tz.redismanager.security;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p>安全认证上下文Hodler</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 19:17
 **/
public class SecurityAuthContextHolder {

    private static final ThreadLocal<SecurityAuthContext> TOKEN_CONTEXT_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static SecurityAuthContext get() {
        return TOKEN_CONTEXT_THREAD_LOCAL.get();
    }

    public static void set(SecurityAuthContext authContext) {
        TOKEN_CONTEXT_THREAD_LOCAL.set(authContext);
    }

    public static void remove() {
        TOKEN_CONTEXT_THREAD_LOCAL.remove();
    }
}
