package com.tz.redismanager.exception;

import com.tz.redismanager.enm.ResultCode;

/**
 * RedisManager异常
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-07-02 20:16
 **/
public class RmException extends RuntimeException {

    private String code;

    public RmException(Throwable cause) {
        super(cause);
    }

    public RmException(String message, Throwable cause) {
        super(message, cause);
    }

    public RmException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public RmException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.code = resultCode.getCode();
    }

    public RmException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RmException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
