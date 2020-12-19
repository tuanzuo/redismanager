package com.tz.redismanager.domain.vo;

import com.tz.redismanager.validator.ValidGroup;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/**
 * <p>验证码VO</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-19 19:06
 **/
@Getter
@Setter
public class CaptchaVO {

    /**
     * 验证码的缓存key  v1.6.0
     */
    @NotEmpty(message = "验证码KEY不能为空", groups = {ValidGroup.AddUserInfo.class, ValidGroup.Login.class})
    private String captchaKey;

    /**
     * 验证码 v1.6.0
     */
    @NotEmpty(message = "验证码不能为空", groups = {ValidGroup.AddUserInfo.class, ValidGroup.Login.class})
    private String captcha;

    public CaptchaVO() {
    }

    public CaptchaVO(String captchaKey, String captcha) {
        this.captchaKey = captchaKey;
        this.captcha = captcha;
    }
}
