package com.tz.redismanager.service.impl;

import com.google.common.collect.Sets;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.token.TokenContext;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * <p>Auth缓存服务</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-09-29 22:12
 **/
@Service
public class AuthCacheServiceImpl implements IAuthCacheService {

    private static final Logger logger = TraceLoggerFactory.getLogger(AuthCacheServiceImpl.class);

    //12小时
    private static long userInfoExpireTime = 12 * 60 * 60;

    @Value("${rd.encrypt.md5Salt}")
    private String md5Salt;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setAuthInfo(String userName, String encodePwd, TokenContext context) {
        String userEncodeKey = this.getUserEncodeKey(userName, encodePwd);
        String authKey = ConstInterface.CacheKey.USER_AUTH + context.getToken();
        String toAuthKey = ConstInterface.CacheKey.USER_TO_AUTH + userEncodeKey;
        context.setToToken(userEncodeKey);
        stringRedisTemplate.opsForValue().set(authKey, JsonUtils.toJsonStr(context), userInfoExpireTime, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(toAuthKey, context.getToken(), userInfoExpireTime, TimeUnit.SECONDS);
    }

    @Override
    public void delAuthInfo(String userName, String encodePwd) {
        Set<String> delKeys = new HashSet<>();
        String userEncodeKey = this.getUserEncodeKey(userName, encodePwd);
        String toAuthKey = ConstInterface.CacheKey.USER_TO_AUTH + userEncodeKey;
        delKeys.add(toAuthKey);
        String token = stringRedisTemplate.opsForValue().get(toAuthKey);
        if (StringUtils.isNotBlank(token)) {
            String authKey = ConstInterface.CacheKey.USER_AUTH + token;
            delKeys.add(authKey);
        }
        stringRedisTemplate.delete(delKeys);
    }

    @Override
    public void delAuthInfoToLogout(TokenContext tokenContext) {
        String authKey = ConstInterface.CacheKey.USER_AUTH + tokenContext.getToken();
        String toAuthKey = ConstInterface.CacheKey.USER_TO_AUTH + tokenContext.getToToken();
        stringRedisTemplate.delete(Sets.newHashSet(authKey, toAuthKey));
    }

    private String getUserEncodeKey(String userName, String encodePwd) {
        return DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", userName, encodePwd, md5Salt).getBytes());
    }


}
