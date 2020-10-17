package com.tz.redismanager.enm;

import org.apache.commons.lang3.StringUtils;

/**
 * 日期类型枚举
 *
 * @version 1.5.0
 * @time 2020-10-17 15:37
 **/
public enum DateTypeEnum {

    TODAY("today"), WEEK("week"), MONTH("month"), YEAR("year"), ALL("all");

    private String type;

    DateTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static DateTypeEnum getEnumByType(String type) {
        if (StringUtils.isBlank(type)) {
            return null;
        }
        for (DateTypeEnum enm : DateTypeEnum.values()) {
            if (null != enm && enm.type.equals(type.trim())) {
                return enm;
            }
        }
        return null;
    }
}
