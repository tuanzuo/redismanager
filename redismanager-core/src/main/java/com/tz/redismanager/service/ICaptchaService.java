package com.tz.redismanager.service;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.CaptchaResp;
import com.tz.redismanager.domain.vo.CaptchaVO;

/**
 * <p>验证码服务接口</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-19 19:10
 **/
public interface ICaptchaService {

    /**
     * 生成验证码
     *
     * @return
     */
    CaptchaResp captcha();

    /**
     * 校验验证码
     *
     * @param vo
     * @return
     */
    ApiResult<?> validCaptcha(CaptchaVO vo);
}
