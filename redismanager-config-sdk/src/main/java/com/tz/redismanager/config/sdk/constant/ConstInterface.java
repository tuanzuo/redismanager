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
        String BASE_PATH = "/dynamic/config/";
    }

}
