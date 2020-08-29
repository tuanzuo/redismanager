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
import com.tz.redismanager.service.IAuthService;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p></p>
 *
 * @version 1.3.0
 * @time 2020-08-29 13:50
 **/
@Service
public class AuthServiceImpl implements IAuthService {

    private static final Logger logger = TraceLoggerFactory.getLogger(AuthServiceImpl.class);

    //12小时
    private static long userInfoExpireTime = 12*60*60;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private UserRoleRelationPOMapper userRoleRelationPOMapper;

    @Override
    public ApiResult<AuthResp> login(LoginVO vo) {
        UserPO userPO = userPOMapper.selectByNamePwd(vo.getName(), vo.getPwd());
        if (null == userPO) {
            return new ApiResult<>(ResultCode.LOGIN_FAIL);
        }
        List<RolePO> roles = userRoleRelationPOMapper.selectByUser(userPO.getId());
        AuthResp resp = this.buildLoginResp(userPO, roles);
        String userInfoKey = ConstInterface.CacheKey.USER_INFO + resp.getToken();
        stringRedisTemplate.opsForValue().set(userInfoKey, JsonUtils.toJsonStr(resp), userInfoExpireTime, TimeUnit.SECONDS);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    @Override
    public ApiResult<Object> logout(String token) {
        String userInfoKey = ConstInterface.CacheKey.USER_INFO + token;
        stringRedisTemplate.delete(userInfoKey);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private AuthResp buildLoginResp(UserPO userPO, List<RolePO> roles) {
        AuthResp resp = new AuthResp();
        resp.setToken(UUIDUtils.generateId() + ConstInterface.Symbol.UNDERLINE + userPO.getName());
        resp.getUser().setName(userPO.getName());
        roles = Optional.ofNullable(roles).orElse(new ArrayList<>());
        resp.setRules(roles.stream().filter(role -> StringUtils.isNotBlank(role.getCode())).map(role -> role.getCode()).collect(Collectors.toSet()));
        return resp;
    }


}
