package com.tz.redismanager.dao.domain.dto;

import com.tz.redismanager.dao.domain.po.RedisConfigExtPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * <p>redis连接配置DTO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-06-06 13:52
 **/
@Getter
@Setter
@ToString
public class RedisConfigDTO {

    /**
     * 主键<br/>
     * v1.7.0-20211109-需要把id属性的类型从Long修改为String，<br/>
     * 因为js的number类型有个最大值（安全值）。即2的53次方=9007199254740992，如果超过这个值，那么js会出现不精确的问题，<br/>
     * 例如：数据库中的id=21827466180016128，但是页面中展示的id=21827466180016130<br/>
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否公开[1=是,0=否] v1.5.0
     *
     * @see com.tz.redismanager.constant.ConstInterface.IS_PUBLIC
     */
    private Integer isPublic;

    /**
     * 类型[1=单机,2=集群]
     *
     * @see com.tz.redismanager.constant.ConstInterface.TYPE
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
     * @see com.tz.redismanager.constant.ConstInterface.IF_DEL
     */
    private Integer ifDel;

    /**
     * 扩展List
     */
    private List<RedisConfigExtPO> extList;
}
