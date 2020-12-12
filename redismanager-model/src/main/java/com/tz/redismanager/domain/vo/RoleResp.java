package com.tz.redismanager.domain.vo;

import com.tz.redismanager.constant.ConstInterface;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>角色返回对象</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-09-26 16:43
 **/
@Getter
@Setter
public class RoleResp {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 状态[1=启用,0=禁用]
     * {@link ConstInterface.ROLE_STATUS}
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

}
