package com.tz.redismanager.bean;

public enum ResultCode {
    SUCCESS("200", "成功"),
    FAIL("500", "失败");

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
