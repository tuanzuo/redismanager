package com.tz.redismanager.config.sdk.domain.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:24
 **/
@Getter
@Setter
public class ConfigQueryDTO {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 服务名
     */
    private String serviceName;

}
