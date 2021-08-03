package com.tz.redismanager.token.service.impl;

import com.tz.redismanager.token.config.TokenProperties;
import com.tz.redismanager.token.constant.ConstInterface;
import com.tz.redismanager.token.domain.ResultCode;
import com.tz.redismanager.token.exception.TokenException;
import com.tz.redismanager.token.service.ITokenService;
import com.tz.redismanager.token.util.AESUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>JWT实现Token</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-11-06 0:39
 **/
@Service
@ConditionalOnClass(Jwts.class)
public class JWTTokenServiceImpl implements ITokenService {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenServiceImpl.class);

    private static final String TOKEN_CONTEXT_KEY = "tokenContext";

    @Autowired
    private TokenProperties tokenProperties;

    @Override
    public boolean support(String tokenType) {
        return ConstInterface.TokenType.JWT.equals(tokenType);
    }

    @Override
    public String getToken(String tokenContext) {
        //加密
        tokenContext = this.encryptTokenContext(tokenContext);
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put(TOKEN_CONTEXT_KEY, tokenContext);
        String token = Jwts.builder()
                .addClaims(claims)
                .setExpiration(new DateTime().plusMinutes(tokenProperties.getExpireTimeToMinutes().intValue()).toDate())
                .signWith(SignatureAlgorithm.HS512, tokenProperties.getJwt().getSignKey())
                .compact();
        return token;
    }

    @Override
    public String resolveToken(String token) {
        //解析token
        Claims clm = null;
        try {
            clm = Jwts.parser()
                    .setSigningKey(tokenProperties.getJwt().getSignKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenException(ResultCode.TOKEN_AUTH_EXPIRE);
        } catch (Exception e) {
            throw new TokenException(ResultCode.TOKEN_AUTH_ERR);
        }
        clm = Optional.ofNullable(clm).orElseThrow(() -> new TokenException(ResultCode.TOKEN_AUTH_EXPIRE));
        String tokenContext = String.valueOf(clm.get(TOKEN_CONTEXT_KEY));
        if (StringUtils.isBlank(tokenContext)) {
            throw new TokenException(ResultCode.TOKEN_AUTH_EXPIRE);
        }
        //解密
        tokenContext = this.decryptTokenContext(tokenContext);
        return tokenContext;
    }

    @Override
    public void clearToken(String token) {

    }


    /**
     * 加密AuthContext
     *
     * @param tokenContext
     * @return
     */
    private String encryptTokenContext(String tokenContext) {
        this.checkJWTConfig();
        try {
            if (tokenProperties.getJwt().getPayloadEncryptEnable()) {
                tokenContext = AESUtils.encrypt(tokenContext, tokenProperties.getJwt().getPayloadEncryptToAESKey());
            }
        } catch (Exception e) {
            logger.error("[JWT]-[加密payload异常]", e);
            throw new TokenException(ResultCode.TOKEN_AUTH_ERR);
        }
        return tokenContext;
    }

    /**
     * 解密AuthContext
     *
     * @param tokenContext
     * @return
     */
    private String decryptTokenContext(String tokenContext) {
        this.checkJWTConfig();
        try {
            if (tokenProperties.getJwt().getPayloadEncryptEnable()) {
                tokenContext = AESUtils.decrypt(tokenContext, tokenProperties.getJwt().getPayloadEncryptToAESKey());
            }
        } catch (Exception e) {
            logger.error("[JWT]-[解密payload异常]-EncryptContext:{}", tokenContext, e);
            throw new TokenException(ResultCode.TOKEN_AUTH_ERR);
        }
        return tokenContext;
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
            throw new TokenException(ResultCode.TOKEN_PROPERTIES_CONFIG_DEFICIENCY);
        }
    }
}
