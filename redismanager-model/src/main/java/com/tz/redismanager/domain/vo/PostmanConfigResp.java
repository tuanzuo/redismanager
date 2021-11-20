package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>postman配置响应</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 20:59
 **/
@Getter
@Setter
@ToString
public class PostmanConfigResp implements Serializable {

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
     * @see com.tz.redismanager.constant.ConstInterface.CATEGORY
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
     * 子列表
     */
    private List<PostmanConfigResp> subList = new ArrayList<>();

}
