package com.tz.redismanager.limiter.domain;

/**
 * 返回code枚举
 *
 * @Since:2019-08-23 22:40:26
 * @Version:1.6.0
 */
public enum ResultCode {

    LIMIT_EXCEPTION("900001", "超过限流阀值"),
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
