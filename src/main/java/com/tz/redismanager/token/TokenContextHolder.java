package com.tz.redismanager.token;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p>Token上下文Hodler</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 19:17
 **/
public class TokenContextHolder {

    private static final ThreadLocal<TokenContext> TOKEN_CONTEXT_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static TokenContext get() {
        return TOKEN_CONTEXT_THREAD_LOCAL.get();
    }

    public static void set(TokenContext tokenContext) {
        TOKEN_CONTEXT_THREAD_LOCAL.set(tokenContext);
    }

    public static void remove() {
        TOKEN_CONTEXT_THREAD_LOCAL.remove();
    }
}
