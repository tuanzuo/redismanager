package com.tz.redismanager.dao.domain.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>redis连接扩展配置PO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:52
 **/
@Getter
@Setter
@ToString
public class RedisConfigExtPO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 扩展key--jarPath(jar包的路径)
     */
    public static final String EXT_KEY_JAR_PATH = "jarPath";

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联的配置id
     */
    private String rconfigId;

    /**
     * 扩展key
     */
    private String extKey;

    /**
     * 扩展名称
     */
    private String extName;

    /**
     * 扩展value
     */
    private String extValue;

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
     * @see com.tz.redismanager.constant.ConstInterface.IF_DEL
     */
    private Integer ifDel;

}