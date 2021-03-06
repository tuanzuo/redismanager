package com.tz.redismanager.cacher.domain;

/**
 * 返回code枚举
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 */
public enum ResultCode {

    GET_ORI_RESULT_EXCEPTION("800001", "查询数据异常"),
    ENABLE_CACHER_TYPE_NOT_SUPPORT("800002", "@EnableCacherAutoConfiguration.cacherType is not support"),
    CACHE_LOCK_EXCEPTION("800003", "加锁异常"),
    CACHE_TRY_LOCK_WAIT_TIMEOUT("800004", "尝试加锁超时"),
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
