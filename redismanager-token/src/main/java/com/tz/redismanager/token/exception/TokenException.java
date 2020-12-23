package com.tz.redismanager.token.exception;


import com.tz.redismanager.token.domain.ResultCode;

/**
 * <p>限流器异常</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 **/
public class TokenException extends RuntimeException {

    private String code;

    public TokenException(Throwable cause) {
        super(cause);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public TokenException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.code = resultCode.getCode();
    }

    public TokenException(String code, String message) {
        super(message);
        this.code = code;
    }

    public TokenException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }


    public String getCode() {
        return code;
    }
}
