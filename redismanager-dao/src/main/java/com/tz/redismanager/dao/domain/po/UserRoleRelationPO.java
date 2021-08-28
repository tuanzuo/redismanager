package com.tz.redismanager.dao.domain.po;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户角色关系PO
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Getter
@Setter
@ToString
public class UserRoleRelationPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

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
