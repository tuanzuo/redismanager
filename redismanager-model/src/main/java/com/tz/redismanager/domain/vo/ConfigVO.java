package com.tz.redismanager.domain.vo;

import com.tz.redismanager.validator.ValidGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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
     * 配置id
     */
    @NotNull(message = "配置id不能为空", groups = {ValidGroup.UpdateConfig.class})
    private Integer id;

    /**
     * 类型
     * {@link com.tz.redismanager.config.enm.ConfigTypeEnum}
     */
    @NotNull(message = "类型不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private Integer type;

    /**
     * 服务名
     */
    @NotEmpty(message = "服务名不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String serviceName;

    /**
     * 配置key
     */
    @NotEmpty(message = "配置key不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String key;

    /**
     * 配置key名称
     */
    @NotEmpty(message = "配置key名称不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String keyName;

    /**
     * 配置内容
     */
    @NotNull(message = "配置内容不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String content;

    /**
     * 版本号
     */
    private Integer version = 1;

    /**
     * 备注
     */
    private String note;
}