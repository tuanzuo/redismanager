package com.tz.redismanager.security;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>Redis实现Token认证</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:39
 **/
public class RedisTokenAuthServiceImpl implements ITokenAuthService {

    private IAuthCacheService authCacheService;
    private ICipherService cipherService;
    private StringRedisTemplate stringRedisTemplate;

    public RedisTokenAuthServiceImpl(IAuthCacheService authCacheService, ICipherService cipherService, StringRedisTemplate stringRedisTemplate) {
        this.authCacheService = authCacheService;
        this.cipherService = cipherService;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String handleLogin(UserPO userPO, AuthContext context) {
        String token = cipherService.encodeUserInfoByMd5(userPO.getName(), userPO.getPwd(), UUIDUtils.generateId());
        context.setToken(token);
        //删除auth缓存数据
        authCacheService.delAuthInfo(userPO.getName(), userPO.getPwd());
        //重新设置auth缓存数据
        authCacheService.setAuthInfo(userPO.getName(), userPO.getPwd(), context);
        return token;
    }

    @Override
    public void handleLogout(AuthContext context) {
        authCacheService.delAuthInfoToLogout(context);
    }

    @Override
    public AuthContext getAuthContext(String token) {
        String userInfoKey = ConstInterface.CacheKey.USER_AUTH + token;
        String authContextCache = stringRedisTemplate.opsForValue().get(userInfoKey);
        if (StringUtils.isBlank(authContextCache)) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        AuthContext authContext = JsonUtils.parseObject(authContextCache, AuthContext.class);
        if (null == authContext) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        return authContext;
    }

}
