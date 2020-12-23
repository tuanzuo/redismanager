package com.tz.redismanager.token.service;

/**
 * <p>Token服务接口</p>
 *
 * @version 1.5.0
 * @time 2020-11-06 0:38
 **/
public interface ITokenService {

    boolean support(String tokenType);

    String getToken(String tokenContext);

    String resolveToken(String token);

    void clearToken(String token);
}
