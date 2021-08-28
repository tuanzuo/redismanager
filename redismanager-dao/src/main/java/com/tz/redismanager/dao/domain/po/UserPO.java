package com.tz.redismanager.dao.domain.po;

import com.tz.redismanager.constant.ConstInterface;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * 用户PO
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:43
 **/
@Getter
@Setter
@ToString
public class UserPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 状态[1=启用,0=禁用]
     * {@link ConstInterface.USER_STATUS}
     */
    private Integer status;

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
     * {@link ConstInterface.IF_DEL}
     */
    private Integer ifDel;

}
