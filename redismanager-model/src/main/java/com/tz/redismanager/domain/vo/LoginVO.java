package com.tz.redismanager.domain.vo;

import javax.validation.constraints.NotEmpty;

/**
 * 登录VO
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
public class LoginVO {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
