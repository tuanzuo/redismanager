package com.tz.redismanager.cacher.exception;

import com.tz.redismanager.cacher.domain.ResultCode;

/**
 * <p>缓存器异常</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-20 17:41
 **/
public class CacherException extends RuntimeException {

    private String code;

    public CacherException(Throwable cause) {
        super(cause);
    }

    public CacherException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacherException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }

    public CacherException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMsg(), cause);
        this.code = resultCode.getCode();
    }

    public CacherException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CacherException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
