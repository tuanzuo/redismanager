package com.tz.redismanager.enm;

import org.apache.commons.lang3.StringUtils;

/**
 * 处理器类型枚举
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-23 21:47
 **/
public enum HandlerTypeEnum {
    STRING("string"), LIST("list"), HASH("hash"), SET("set"), ZSET("zset");

    private String type;

    HandlerTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static HandlerTypeEnum getEnumByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (HandlerTypeEnum enm : HandlerTypeEnum.values()) {
            if (enm.getType().equals(type.trim())) {
                return enm;
            }
        }
        return null;
    }
}
