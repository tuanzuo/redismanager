package com.tz.redismanager.security.token.impl;

import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.security.token.ITokenService;
import com.tz.redismanager.security.token.config.TokenProperties;
import com.tz.redismanager.util.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>JWT实现Token</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:39
 **/
public class JWTTokenServiceImpl implements ITokenService {

    private static final String KEY = "authContext";
    private TokenProperties tokenProperties;

    public JWTTokenServiceImpl(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public String handleLogin(UserPO userPO, AuthContext context) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(KEY, JsonUtils.toJsonStr(context));
        String token = Jwts.builder()
                .addClaims(claims)
                .setExpiration(new DateTime().plusMinutes(tokenProperties.getExpireTimeToMinutes().intValue()).toDate())
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getJwt().getSignKey())
                .compact();
        return token;
    }

    @Override
    public void handleLogout(AuthContext context) {

    }

    @Override
    public AuthContext getAuthContext(String token) {
        Claims clm = null;
        try {
            clm = Jwts.parser().setSigningKey(tokenProperties.getJwt().getSignKey()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        } catch (Exception e) {
            throw new RmException(ResultCode.TOKEN_AUTH_ERR);
        }
        clm = Optional.ofNullable(clm).orElseThrow(() -> new RmException(ResultCode.TOKEN_AUTH_EXPIRE));
        String authContextStr = String.valueOf(clm.get(KEY));
        if (StringUtils.isBlank(authContextStr)) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        AuthContext authContext = JsonUtils.parseObject(authContextStr, AuthContext.class);
        return Optional.ofNullable(authContext).orElseThrow(() -> new RmException(ResultCode.TOKEN_AUTH_EXPIRE));
    }
}
