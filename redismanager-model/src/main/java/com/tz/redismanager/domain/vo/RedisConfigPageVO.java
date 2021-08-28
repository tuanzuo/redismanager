package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>redis连接分页数据VO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-28 23:24
 **/
@Getter
@Setter
public class RedisConfigPageVO {

    /**
     * redis连接配置列表
     */
    List<?> configList = new ArrayList<>();

    /**
     * 当前redis连接配置列表中最小的id
     */
    Long currentMinId = 0L;
}
