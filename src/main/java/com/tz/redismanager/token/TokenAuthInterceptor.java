package com.tz.redismanager.token;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * <p>TokenAuth拦截器</p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
public class TokenAuthInterceptor extends HandlerInterceptorAdapter {

    private StringRedisTemplate stringRedisTemplate;

    public TokenAuthInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        this.handleTokenContext(request, handler);
        return super.preHandle(request, response, handler);
    }

    private void handleTokenContext(HttpServletRequest request, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        TokenAuth tokenAuth = handlerMethod.getMethodAnnotation(TokenAuth.class);
        if (null == tokenAuth) {
            return;
        }
        String token = request.getHeader(ConstInterface.Auth.AUTHORIZATION);
        TokenContext tokenContext = new TokenContext();
        tokenContext.setToken(token);
        try {
            //验证token
            if (StringUtils.isBlank(token)) {
                throw new RmException(ResultCode.TOKEN_AUTH_ERR);
            }
            String userInfoKey = ConstInterface.CacheKey.USER_AUTH + token;
            String tokenContextCache = stringRedisTemplate.opsForValue().get(userInfoKey);
            if (StringUtils.isBlank(tokenContextCache)) {
                throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
            }
            tokenContext = JsonUtils.parseObject(tokenContextCache, TokenContext.class);
            if (null == tokenContext) {
                throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
            }
            //验证角色
            if (ArrayUtils.isNotEmpty(tokenAuth.permitRoles())) {
                Set<String> roles = Optional.ofNullable(tokenContext.getRoles()).orElse(new HashSet<>());
                Optional<String> roleOptional = Arrays.stream(tokenAuth.permitRoles()).filter(role -> roles.contains(role)).limit(1).findAny();
                if (!roleOptional.isPresent()) {
                    throw new RmException(ResultCode.ROLE_AUTH_FAIL);
                }
            }
        } catch (Throwable e) {
            if (tokenAuth.required()) {
                throw e;
            }
        }
        TokenContextHolder.set(tokenContext);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TokenAuth tokenAuth = handlerMethod.getMethodAnnotation(TokenAuth.class);
            if (null != tokenAuth) {
                TokenContextHolder.remove();
            }
        }
    }

}
