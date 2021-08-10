package com.tz.redismanager.config.sdk.domain.dto;

import com.tz.redismanager.config.sdk.enm.ConfigTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>配置Context</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:16
 **/
@Getter
@Setter
@ToString
public class ConfigContext {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 配置类型
     * {@link ConfigTypeEnum}
     */
    private Integer configType;

    /**
     * 配置key
     */
    private String configKey;

    /**
     * 配置key名称
     */
    private String keyName;

    /**
     * 配置内容JSON
     */
    private String content;

}