package com.tz.redismanager.config.domain.dto;

import com.tz.redismanager.config.domain.po.ConfigPO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * <p>配置DTO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-24 0:00
 **/
@Getter
@Setter
public class ConfigDTO extends ConfigPO {

    /**
     * 配置id集合
     */
    private Set<Long> ids;
}
