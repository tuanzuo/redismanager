package com.tz.redismanager.domain.vo;

import com.tz.redismanager.validator.ValidGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * <p>配置VO</p>
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
    private Long id;

    /**
     * 配置id集合
     */
    @NotEmpty(message = "配置id集合不能为空", groups = {ValidGroup.DelConfig.class})
    private Set<Long> ids;

    /**
     * 服务名
     */
    @NotEmpty(message = "服务名不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String serviceName;

    /**
     * 配置类型
     * {@link com.tz.redismanager.config.enm.ConfigTypeEnum}
     */
    @NotNull(message = "类型不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private Integer configType;

    /**
     * 配置key
     */
    @NotEmpty(message = "配置key不能为空", groups = {ValidGroup.AddConfig.class, ValidGroup.UpdateConfig.class})
    private String configKey;

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
