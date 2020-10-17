package com.tz.redismanager.service.impl;

import com.google.common.collect.Sets;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.security.AuthContext;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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

    @Autowired
    private ICipherService cipherService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void setAuthInfo(String userName, String encodePwd, AuthContext authContext) {
        String userEncodeKey = cipherService.encodeUserInfoByMd5(userName, encodePwd);
        String authKey = ConstInterface.CacheKey.USER_AUTH + authContext.getToken();
        String toAuthKey = ConstInterface.CacheKey.USER_TO_AUTH + userEncodeKey;
        authContext.setToToken(userEncodeKey);
        stringRedisTemplate.opsForValue().set(authKey, JsonUtils.toJsonStr(authContext), userInfoExpireTime, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(toAuthKey, authContext.getToken(), userInfoExpireTime, TimeUnit.SECONDS);
    }

    @Override
    public void delAuthInfo(String userName, String encodePwd) {
        Set<String> delKeys = new HashSet<>();
        String userEncodeKey = cipherService.encodeUserInfoByMd5(userName, encodePwd);
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
    public void delAuthInfoToLogout(AuthContext authContext) {
        String authKey = ConstInterface.CacheKey.USER_AUTH + authContext.getToken();
        String toAuthKey = ConstInterface.CacheKey.USER_TO_AUTH + authContext.getToToken();
        stringRedisTemplate.delete(Sets.newHashSet(authKey, toAuthKey));
    }

}