package com.tz.redismanager.config.exception;

/**
 * <p>配置自定义异常</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-24 16:36
 **/
public class ConfigException extends RuntimeException {

    private String code;

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(String code, String message) {
        super(message);
        this.code = code;
    }

    public ConfigException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
