package com.tz.redismanager.token.domain;

/**
 * 返回code枚举
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 */
public enum ResultCode {

    TOKEN_AUTH_ERR("700001", "token验证失败"),
    TOKEN_AUTH_EXPIRE("700002", "token过期,请重新登录"),
    ENABLE_TOKEN_NOT_PRESENT("700003", "@EnableTokenAutoConfiguration is not present"),
    ENABLE_TOKEN_TYPE_NOT_SUPPORT("700004", "@EnableTokenAutoConfiguration tokenType is not support"),
    TOKEN_PROPERTIES_CONFIG_DEFICIENCY("700005", "TokenProperties配置缺失"),
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
