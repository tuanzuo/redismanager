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
    REDIS_KEY_EXIST("600002", "key已经存在,不能添加");

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
