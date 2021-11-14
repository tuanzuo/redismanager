package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * <p>postman配置VO</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 20:59
 **/
@Getter
@Setter
@ToString
public class PostmanConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 是否公开[1=是,0=否]
     */
    private Integer shareFlag;

    /**
     * 类型[1=接口分类,2=接口,3=环境]
     */
    private Integer category;

    /**
     * 名称
     */
    private String configName;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 配置信息JSON
     */
    private String configInfo;

    /**
     * 备注
     */
    private String note;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否删除[1=是,0=否]
     */
    private Integer ifDel;

    /**
     * 分类名称
     */
    private String categoryName;

}
