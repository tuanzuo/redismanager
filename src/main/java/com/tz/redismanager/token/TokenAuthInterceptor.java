package com.tz.redismanager.token;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p></p>
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
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            TokenAuth tokenAuth = handlerMethod.getMethodAnnotation(TokenAuth.class);
            if (null != tokenAuth) {
                String token = request.getHeader(ConstInterface.Auth.AUTHORIZATION);
                if (StringUtils.isBlank(token)) {
                    throw new RmException(ResultCode.TOKEN_AUTH_ERR);
                }
                String userInfoKey = ConstInterface.CacheKey.USER_INFO + token;
                String authStr = stringRedisTemplate.opsForValue().get(userInfoKey);
                if (StringUtils.isBlank(authStr)) {
                    throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
                }
                AuthResp authResp = JsonUtils.parseObject(authStr, AuthResp.class);
                if (null == authResp) {
                    throw new RmException(ResultCode.TOKEN_AUTH_EXPIRE);
                }
                TokenContext tokenContext = new TokenContext();
                tokenContext.setUserName(authResp.getUser().getName());
                tokenContext.setToken(authResp.getToken());
                TokenContextHolder.set(tokenContext);
            }
        }
        return super.preHandle(request, response, handler);
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
