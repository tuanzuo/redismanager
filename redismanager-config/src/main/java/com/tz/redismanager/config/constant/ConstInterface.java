package com.tz.redismanager.config.constant;

/**
 * <p>常量接口</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-09 23:17
 **/
public interface ConstInterface {

    interface IF_DEL {
        Integer YES = 1;
        Integer NO = 0;
    }

    interface Zookeeper {
        //路径前缀
        String PRE_PATH = "/dynamic/config/";
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
