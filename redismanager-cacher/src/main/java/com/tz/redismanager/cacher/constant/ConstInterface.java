package com.tz.redismanager.cacher.constant;

/**
 * 常量接口
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-21 22:11
 */
public interface ConstInterface {

    /**
     * 缓存器类型
     */
    interface CacherType {
        //默认缓存器类型
        String DEFAULT_CACHER = "defaultCacher";
    }

    /**
     * 符号
     */
    interface Symbol {
        String COLON = ":";
        String SPACE = " ";
        String BLANK = "";
        String COMMA = ",";
        String STAR = "*";
        String SPOT = ".";
        String UNDER_LINE = "_";
        String MIDDLE_LINE = "-";
        String MIDDLE_BRACKET_LEFT = "[";
        String MIDDLE_BRACKET_RIGHT = "]";
        String BRACKET_LEFT = "(";
        String BRACKET_RIGHT = ")";
        String SLASH = "/";
    }
}
