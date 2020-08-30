package com.tz.redismanager.enm;

/**
 * 返回code枚举
 *
 * @Since:2019-08-23 22:40:26
 * @Version:1.1.0
 */
public enum ResultCode {
    SUCCESS("200", "成功"),
    FAIL("500", "失败"),
    REDIS_TEMPLATE_ISNULL("600001", "RedisTemplate为空"),
    REDIS_KEY_EXIST("600002", "key已经存在,不能添加"),
    LOGIN_FAIL("600003", "用户名或者密码不正确"),
    UPDATE_PWD_FAIL("600004", "密码修改失败，请检查原密码是否正确"),

    TOKEN_AUTH_ERR("700001", "token验证失败"),
    TOKEN_AUTH_EXPIRE("700002", "token过期,请重新登录"),
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
