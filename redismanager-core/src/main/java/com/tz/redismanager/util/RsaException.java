package com.tz.redismanager.util;

import com.tz.redismanager.constant.ConstInterface;
import org.apache.commons.lang3.StringUtils;

/**
 * 加解密异常
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:53:29
 * @Version:1.1.0
 */
public class RsaException extends RuntimeException {

    private String errCode;
    private String errMsg;

    public RsaException() {
        super();
    }

    public RsaException(String message, Throwable cause) {
        super(message, cause);
    }

    public RsaException(String message) {
        super(message);
    }

    public RsaException(Throwable cause) {
        super(cause);
    }

    public RsaException(String errCode, String errMsg) {
        super(StringUtils.join(errCode, ConstInterface.Symbol.COLON, errMsg));
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return this.errCode;
    }

    public String getErrMsg() {
        return this.errMsg;
    }

}