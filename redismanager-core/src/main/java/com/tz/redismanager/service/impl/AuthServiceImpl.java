package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RoleDTO;
import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.domain.vo.CaptchaVO;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.security.domain.AuthContextToRedis;
import com.tz.redismanager.service.IAuthService;
import com.tz.redismanager.service.ICaptchaService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.service.IStatisticService;
import com.tz.redismanager.token.service.ITokenService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>授权service实现</p>
 *
 * @author tuanzuo
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = TraceLoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private ICipherService cipherService;
    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private UserRoleRelationPOMapper userRoleRelationPOMapper;
    @Autowired
    private ITokenService tokenAuthService;
    @Autowired
    private IStatisticService userStatisticsService;

    @Override
    public ApiResult<AuthResp> login(LoginVO vo) {
        //校验验证码
        ApiResult<?> validCaptchaResult = captchaService.validCaptcha(new CaptchaVO(vo.getCaptchaKey(), vo.getCaptcha()));
        if (!ResultCode.SUCCESS.getCode().equals(validCaptchaResult.getCode())) {
            return new ApiResult<>(validCaptchaResult.getCode(), validCaptchaResult.getMsg());
        }

        String encodePwd = cipherService.encodeUserInfoByMd5(vo.getName(), vo.getPwd());
        UserPO userPO = userPOMapper.selectByNamePwd(vo.getName(), encodePwd, ConstInterface.IF_DEL.NO);
        if (null == userPO) {
            return new ApiResult<>(ResultCode.LOGIN_FAIL);
        }
        if (ConstInterface.USER_STATUS.DISABLE.equals(userPO.getStatus())) {
            return new ApiResult<>(ResultCode.USER_DISABLE);
        }
        List<RoleDTO> roles = userRoleRelationPOMapper.selectByUserRole(userPO.getId(), null,
                ConstInterface.ROLE_STATUS.ENABLE, ConstInterface.IF_DEL.NO);
        AuthResp resp = this.buildLoginResp(roles);
        AuthContext context = this.buildAuthContext(userPO, resp);
        String tokenContext = JsonUtils.toJsonStr(context);
        if (tokenAuthService.support(ConstInterface.TokenType.REDIS)) {
            AuthContextToRedis authContextToRedis = new AuthContextToRedis();
            authContextToRedis.setUserPO(userPO);
            authContextToRedis.setAuthContext(context);
            tokenContext = JsonUtils.toJsonStr(authContextToRedis);
        }
        String token = tokenAuthService.getToken(tokenContext);
        resp.setToken(token);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    @Override
    public ApiResult<Object> logout(AuthContext context) {
        tokenAuthService.clearToken(JsonUtils.toJsonStr(context));
        userStatisticsService.removeOnlineUser(context.getUserId());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private AuthResp buildLoginResp(List<RoleDTO> roles) {
        AuthResp resp = new AuthResp();
        roles = Optional.ofNullable(roles).orElse(new ArrayList<>());
        resp.setRoles(roles.stream().filter(role -> StringUtils.isNotBlank(role.getCode())).map(role -> role.getCode()).collect(Collectors.toSet()));
        return resp;
    }

    private AuthContext buildAuthContext(UserPO user, AuthResp resp) {
        AuthContext context = new AuthContext();
        context.setUserId(user.getId());
        context.setUserName(user.getName());
        context.setRoles(resp.getRoles());
        return context;
    }

}
