package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 21:27
 **/
@Getter
@Setter
public class ConfigVO {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 类型
     * {@link com.tz.redismanager.config.enm.ConfigTypeEnum}
     */
    private Integer type;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 配置key
     */
    private String key;

    /**
     * 配置内容
     */
    private Object content;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String note;
}
