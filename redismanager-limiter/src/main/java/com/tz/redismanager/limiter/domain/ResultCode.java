package com.tz.redismanager.limiter.domain;

/**
 * 返回code枚举
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 */
public enum ResultCode {

    LIMIT_EXCEPTION("900001", "超过限流阀值"),
    ENABLE_LIMITER_TYPE_NOT_SUPPORT("900002", "@EnableLimiterAutoConfiguration.limiterType is not support"),

    ;

    private String code;
    private String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
