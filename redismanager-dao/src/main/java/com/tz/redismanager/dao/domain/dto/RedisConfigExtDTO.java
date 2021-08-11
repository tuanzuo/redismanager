package com.tz.redismanager.dao.domain.dto;

import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

/**
 * <p>redis连接扩展配置DTO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:56
 **/
@Getter
@Setter
@ToString
public class RedisConfigExtDTO extends RedisConfigExtPO {

    /**
     * 扩展配置id集合
     */
    private Set<String> rconfigIds;

}
