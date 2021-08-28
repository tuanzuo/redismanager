package com.tz.redismanager.dao.domain.po;

import com.tz.redismanager.constant.ConstInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * redis连接配置PO
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:32:11
 * @Version:1.1.0
 */
@Getter
@Setter
@ToString
public class RedisConfigPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否公开[1=是,0=否] v1.5.0
     *
     * @see ConstInterface.IS_PUBLIC
     */
    private Integer isPublic;

    /**
     * 类型[1=单机,2=集群]
     *
     * @see ConstInterface.TYPE
     */
    private Integer type;

    /**
     * 地址
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 序列化代码
     */
    private String serCode;

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
     *
     * @see ConstInterface.IF_DEL
     */
    private Integer ifDel;

}
