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
    USER_DISABLE("600005", "账号被禁用,请联系管理员"),
    QUERY_NULL("600006", "查询不到数据"),
    EXIST_ROLE_CODE("600007", "角色编码已存在"),
    USER_EXIST("600008", "来晚啦，用户名已被其他小伙伴注册了"),

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
