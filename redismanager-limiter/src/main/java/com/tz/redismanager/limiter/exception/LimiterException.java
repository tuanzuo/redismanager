package com.tz.redismanager.limiter.exception;

import com.tz.redismanager.limiter.domain.ResultCode;

/**
 * <p>限流器异常</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 **/
public class LimiterException extends RuntimeException {

    private String code;

    public LimiterException(Throwable cause) {
        super(cause);
    }

    public LimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimiterException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public LimiterException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.code = resultCode.getCode();
    }

    public LimiterException(String code, String message) {
        super(message);
        this.code = code;
    }

    public LimiterException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
