package com.tz.redismanager.config.enm;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 20:33
 **/
public enum ConfigTypeEnum {
    CACHER(1, "cacher", "缓存配置"),
    LIMITER(2, "limiter", "限流配置"),
    TOKEN(3, "token", "token配置"),
    ;

    private Integer code;
    private String name;
    private String msg;

    ConfigTypeEnum(Integer code, String name, String msg) {
        this.code = code;
        this.name = name;
        this.msg = msg;
    }

    public static ConfigTypeEnum getByTypeCode(Integer code) {
        if (null == code) {
            return null;
        }
        for (ConfigTypeEnum enm : ConfigTypeEnum.values()) {
            if (enm.getCode().equals(code)) {
                return enm;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMsg() {
        return msg;
    }
}
