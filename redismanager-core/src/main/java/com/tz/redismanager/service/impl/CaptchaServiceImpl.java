package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.CaptchaResp;
import com.tz.redismanager.domain.vo.CaptchaVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.ICaptchaService;
import com.tz.redismanager.util.UUIDUtils;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>验证码服务实现</p>
 * {@link https://github.com/whvcse/EasyCaptcha }
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2020-12-19 19:11
 **/
@Service
public class CaptchaServiceImpl implements ICaptchaService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public CaptchaResp captcha() {
        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(120, 35);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的公式：3+2=?
        captcha.getArithmeticString();
        // 获取运算的结果：5
        String verCode = captcha.text().toLowerCase();
        String key = UUIDUtils.generateId();
        // 验证码放入缓存
        stringRedisTemplate.opsForValue().set(StringUtils.join(ConstInterface.CacheKey.CAPTCHA_KEY, key), verCode, 30, TimeUnit.SECONDS);
        return this.buildCaptchaResp(captcha, key);
    }

    @Override
    public ApiResult<?> validCaptcha(CaptchaVO vo) {
        String captchaCacheKey = StringUtils.join(ConstInterface.CacheKey.CAPTCHA_KEY, vo.getCaptchaKey());
        String captchaCode = stringRedisTemplate.opsForValue().get(captchaCacheKey);
        if (StringUtils.isBlank(captchaCode)) {
            return new ApiResult<>(ResultCode.CAPTCHA_EXPIRE);
        }
        if (!captchaCode.equalsIgnoreCase(vo.getCaptcha())) {
            return new ApiResult<>(ResultCode.CAPTCHA_ERROR);
        }
        stringRedisTemplate.delete(captchaCacheKey);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private CaptchaResp buildCaptchaResp(ArithmeticCaptcha captcha, String key) {
        CaptchaResp resp = new CaptchaResp();
        //验证码的缓存key
        resp.setKey(key);
        //图形验证码
        resp.setImage(captcha.toBase64());
        return resp;
    }
}
