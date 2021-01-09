package com.tz.redismanager.security.auth;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.security.annotation.Auth;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.token.exception.TokenException;
import com.tz.redismanager.token.service.ITokenService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
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
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = TraceLoggerFactory.getLogger(AuthInterceptor.class);

    private ITokenService tokenAuthService;
    private IStatisticService statisticService;

    public AuthInterceptor(ITokenService tokenAuthService, IStatisticService statisticService) {
        this.tokenAuthService = tokenAuthService;
        this.statisticService = statisticService;
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
        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
        if (null == auth) {
            return;
        }
        String token = request.getHeader(ConstInterface.Auth.AUTHORIZATION);
        AuthContext authContext = new AuthContext();
        authContext.setToken(token);
        try {
            //验证token
            if (StringUtils.isBlank(token)) {
                throw new TokenException(com.tz.redismanager.token.domain.ResultCode.TOKEN_AUTH_ERR);
            }
            //得到AuthContext
            String authContextStr = tokenAuthService.resolveToken(token);
            try {
                AuthContext authContextParse = JsonUtils.parseObject(authContextStr, AuthContext.class);
                authContext = Optional.ofNullable(authContextParse).orElseThrow(() -> new TokenException(com.tz.redismanager.token.domain.ResultCode.TOKEN_AUTH_EXPIRE));
            } catch (Exception e) {
                logger.error("[解析AuthContext异常]-AuthContext:{}", authContextStr, e);
                throw new TokenException(com.tz.redismanager.token.domain.ResultCode.TOKEN_AUTH_ERR);
            }
            //验证角色
            if (ArrayUtils.isNotEmpty(auth.permitRoles())) {
                Set<String> roles = Optional.ofNullable(authContext.getRoles()).orElse(new HashSet<>());
                Optional<String> roleOptional = Arrays.stream(auth.permitRoles()).filter(role -> roles.contains(role)).limit(1).findAny();
                if (!roleOptional.isPresent()) {
                    throw new RmException(ResultCode.ROLE_AUTH_FAIL);
                }
            }
        } catch (Throwable e) {
            if (auth.required()) {
                throw e;
            }
        }
        statisticService.statisticsToAsync(authContext);
        AuthContextHolder.set(authContext);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
            if (null != auth) {
                AuthContextHolder.remove();
            }
        }
    }

}
