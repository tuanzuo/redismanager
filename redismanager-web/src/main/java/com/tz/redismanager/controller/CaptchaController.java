package com.tz.redismanager.controller;

import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.CaptchaResp;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.limiter.annotation.Limiter;
import com.tz.redismanager.service.ICaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码管理
 *
 * @Author:tuanzuo
 * @Since:2020-12-19 19:17:15
 * @Version:1.6.0
 */
@RestController
public class CaptchaController {

    @Autowired
    private ICaptchaService captchaService;

    /**
     * 获取验证码接口
     * @return
     */
    @RequestMapping("/captcha")
    @Limiter(name = "获取验证码接口限流", key = "CAPTCHA_API", qps = 100)
    public ApiResult<CaptchaResp> captcha() {
        return new ApiResult<>(ResultCode.SUCCESS, captchaService.captcha());
    }


}
