package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * 登录VO
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Getter
@Setter
public class LoginVO extends CaptchaVO{

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String name;

    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String pwd;

}
