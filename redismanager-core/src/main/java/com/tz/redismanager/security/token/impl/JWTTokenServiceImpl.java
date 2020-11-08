package com.tz.redismanager.security.token.impl;

import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.security.token.ITokenService;
import com.tz.redismanager.security.token.config.TokenProperties;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.AESUtils;
import com.tz.redismanager.util.JsonUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;

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

    private static final Logger logger = TraceLoggerFactory.getLogger(JWTTokenServiceImpl.class);

    private static final String AUTH_CONTEXT_KEY = "authContext";

    private TokenProperties tokenProperties;

    public JWTTokenServiceImpl(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    @Override
    public String handleLogin(UserPO userPO, AuthContext context) {
        String jsonContext = JsonUtils.toJsonStr(context);
        //加密
        jsonContext = this.handleEncryptAuthContext(jsonContext);
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(AUTH_CONTEXT_KEY, jsonContext);
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
        //解析token
        Claims clm = null;
        try {
            clm = Jwts.parser()
                    .setSigningKey(tokenProperties.getJwt().getSignKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        } catch (Exception e) {
            throw new RmException(ResultCode.TOKEN_AUTH_ERR);
        }
        clm = Optional.ofNullable(clm).orElseThrow(() -> new RmException(ResultCode.TOKEN_AUTH_EXPIRE));
        String authContextStr = String.valueOf(clm.get(AUTH_CONTEXT_KEY));
        if (StringUtils.isBlank(authContextStr)) {
            throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        //解密
        authContextStr = this.handleDecryptAuthContext(authContextStr);
        try {
            AuthContext authContext = JsonUtils.parseObject(authContextStr, AuthContext.class);
            return Optional.ofNullable(authContext).orElseThrow(() -> new RmException(ResultCode.TOKEN_AUTH_EXPIRE));
        } catch (Exception e) {
            logger.error("[JWT]-[解析AuthContext异常]-AuthContext:{}", authContextStr, e);
            throw new RmException(ResultCode.TOKEN_AUTH_ERR);
        }
    }

    /**
     * 加密AuthContext
     *
     * @param authContextStr
     * @return
     */
    private String handleEncryptAuthContext(String authContextStr) {
        this.checkJWTConfig();
        try {
            if (tokenProperties.getJwt().getPayloadEncryptEnable()) {
                authContextStr = AESUtils.encrypt(authContextStr, tokenProperties.getJwt().getPayloadEncryptToAESKey());
            }
        } catch (Exception e) {
            logger.error("[JWT]-[加密payload异常]", e);
            throw new RmException(ResultCode.TOKEN_AUTH_ERR);
        }
        return authContextStr;
    }

    /**
     * 解密AuthContext
     *
     * @param authContextStr
     * @return
     */
    private String handleDecryptAuthContext(String authContextStr) {
        this.checkJWTConfig();
        try {
            if (tokenProperties.getJwt().getPayloadEncryptEnable()) {
                authContextStr = AESUtils.decrypt(authContextStr, tokenProperties.getJwt().getPayloadEncryptToAESKey());
            }
        } catch (Exception e) {
            logger.error("[JWT]-[解密payload异常]-EncryptContext:{}", authContextStr, e);
            throw new RmException(ResultCode.TOKEN_AUTH_ERR);
        }
        return authContextStr;
    }

    /**
     * 检查JWT配置
     */
    private void checkJWTConfig() {
        Boolean payloadEncryptEnable = tokenProperties.getJwt().getPayloadEncryptEnable();
        payloadEncryptEnable = Optional.ofNullable(payloadEncryptEnable).orElse(false);
        String aesKey = tokenProperties.getJwt().getPayloadEncryptToAESKey();
        if (payloadEncryptEnable && StringUtils.isBlank(aesKey)) {
            logger.error("[JWT]-[已开启payload加密]-[请设置加密的key]-->rm.token.jwt.payloadEncryptToAESKey");
            throw new RmException(ResultCode.FAIL);
        }
    }
}
