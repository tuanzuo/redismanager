package com.tz.redismanager.config.domain.po;

import com.tz.redismanager.config.constant.ConstInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p>配置PO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-02 21:47
 **/
@Getter
@Setter
@ToString
public class ConfigPO {

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
     * 配置key名称
     */
    private String keyName;

    /**
     * 配置内容JSON
     */
    private String content;

    /**
     * 版本号
     */
    private Integer version;

    /**
     * 备注
     */
    private String note;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除[1=是,0=否]
     * @see ConstInterface.IF_DEL
     */
    private Integer ifDel;

}