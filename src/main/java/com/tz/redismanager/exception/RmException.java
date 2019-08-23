package com.tz.redismanager.exception;

/**
 * RedisManager异常
 *
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

    public RmException(String code, String message) {
        super(message);
        this.code = code;
    }

    public RmException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


}
