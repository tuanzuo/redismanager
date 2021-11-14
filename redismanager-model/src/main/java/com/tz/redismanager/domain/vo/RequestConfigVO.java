package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * <p>请求配置VO</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 20:59
 **/
@Getter
@Setter
@ToString
public class RequestConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求类型，例如：get,post
     */
    private String requestType;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * header(JSON格式)
     */
    private String headers;

    /**
     * body(JSON格式)
     */
    private String body;

    /**
     * 参数(JSON格式)
     */
    private String params;

    /**
     * cookie(JSON格式)
     */
    private String cookies;

}
