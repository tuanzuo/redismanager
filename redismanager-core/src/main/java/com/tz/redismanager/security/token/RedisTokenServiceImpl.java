package com.tz.redismanager.security.token;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.security.domain.AuthContextToRedis;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.token.config.TokenProperties;
import com.tz.redismanager.token.domain.ResultCode;
import com.tz.redismanager.token.exception.TokenException;
import com.tz.redismanager.token.service.ITokenService;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>Redis实现Token</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-12-23 21:43
 **/
@Service
public class RedisTokenServiceImpl implements ITokenService {

    @Autowired
    private TokenProperties tokenProperties;
    @Autowired
    private IAuthCacheService authCacheService;
    @Autowired
    private ICipherService cipherService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean support(String tokenType) {
        return ConstInterface.TokenType.REDIS.equals(tokenType);
    }

    @Override
    public String getToken(String tokenContext) {
        AuthContextToRedis authContextToRedis = JsonUtils.parseObject(tokenContext, AuthContextToRedis.class);
        UserPO userPO = authContextToRedis.getUserPO();
        AuthContext context = authContextToRedis.getAuthContext();
        String token = cipherService.encodeUserInfoByMd5(userPO.getName(), userPO.getPwd(), UUIDUtils.generateId());
        context.setToken(token);
        //删除auth缓存数据
        authCacheService.delAuthInfo(userPO.getName(), userPO.getPwd());
        //重新设置auth缓存数据
        authCacheService.setAuthInfo(userPO.getName(), userPO.getPwd(), tokenProperties.getExpireTimeToMinutes(), context);
        return token;
    }

    @Override
    public String resolveToken(String token) {
        String userInfoKey = ConstInterface.CacheKey.USER_AUTH + token;
        String authContextCache = stringRedisTemplate.opsForValue().get(userInfoKey);
        if (StringUtils.isBlank(authContextCache)) {
            throw new TokenException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        return authContextCache;
    }

    @Override
    public void clearToken(String token) {
        authCacheService.delAuthInfoToLogout(JsonUtils.parseObject(token, AuthContext.class));
    }
}
