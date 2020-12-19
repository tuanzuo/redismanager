package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>验证码返回对象</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-19 19:06
 **/
@Getter
@Setter
public class CaptchaResp {

    /**
     * 验证码对应的缓存key
     */
    private String key;

    /**
     * 图形验证码 Base64编码
     */
    private String image;
    
}
