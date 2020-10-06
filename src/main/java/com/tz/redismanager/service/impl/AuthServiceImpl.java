package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.po.UserPO;
import com.tz.redismanager.domain.vo.AuthResp;
import com.tz.redismanager.domain.vo.LoginVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.IAuthService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.token.TokenContext;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>授权service</p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = TraceLoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private ICipherService cipherService;
    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private UserRoleRelationPOMapper userRoleRelationPOMapper;
    @Autowired
    private IAuthCacheService authCacheService;

    @Override
    public ApiResult<AuthResp> login(LoginVO vo) {
        String encodePwd = cipherService.encodeUserInfoByMd5(vo.getName(), vo.getPwd());
        UserPO userPO = userPOMapper.selectByNamePwd(vo.getName(), encodePwd);
        if (null == userPO) {
            return new ApiResult<>(ResultCode.LOGIN_FAIL);
        }
        if (ConstInterface.USER_STATUS.DISABLE.equals(userPO.getStatus())) {
            return new ApiResult<>(ResultCode.USER_DISABLE);
        }

        //删除auth缓存数据
        authCacheService.delAuthInfo(userPO.getName(), userPO.getPwd());
        List<RolePO> roles = userRoleRelationPOMapper.selectByUserRole(userPO.getId(), ConstInterface.ROLE_STATUS.ENABLE);
        AuthResp resp = this.buildLoginResp(userPO, roles);
        TokenContext context = this.buildTokenContext(userPO, resp.getToken());
        //重新设置auth缓存数据
        authCacheService.setAuthInfo(userPO.getName(), userPO.getPwd(), context);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    @Override
    public ApiResult<Object> logout(TokenContext tokenContext) {
        authCacheService.delAuthInfoToLogout(tokenContext);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private AuthResp buildLoginResp(UserPO userPO, List<RolePO> roles) {
        AuthResp resp = new AuthResp();
        String token = cipherService.encodeUserInfoByMd5(userPO.getName(), userPO.getPwd(), UUIDUtils.generateId());
        resp.setToken(token);
        roles = Optional.ofNullable(roles).orElse(new ArrayList<>());
        resp.setRoles(roles.stream().filter(role -> StringUtils.isNotBlank(role.getCode())).map(role -> role.getCode()).collect(Collectors.toSet()));
        return resp;
    }

    private TokenContext buildTokenContext(UserPO user, String token) {
        TokenContext context = new TokenContext();
        context.setUserId(user.getId());
        context.setUserName(user.getName());
        context.setToken(token);
        return context;
    }

}
