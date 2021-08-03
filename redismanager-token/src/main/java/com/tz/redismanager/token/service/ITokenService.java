package com.tz.redismanager.token.service;

/**
 * <p>Token服务接口</p>
 *
 * @author tuanzuo
 * @version 1.5.0
 * @time 2020-11-06 0:38
 **/
public interface ITokenService {

    /**
     * 支持条件
     * @param tokenType
     * @return
     */
    boolean support(String tokenType);

    /**
     * 生成token
     * @param tokenContext
     * @return
     */
    String getToken(String tokenContext);

    /**
     * 解析token
     * @param token
     * @return
     */
    String resolveToken(String token);

    /**
     * 清理token
     * @param token
     */
    void clearToken(String token);
}
