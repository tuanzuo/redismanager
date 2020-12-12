package com.tz.redismanager.service;

/**
 * <p>密码service接口</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-02 20:06
 **/
public interface ICipherService {

    String encodeUserInfoByMd5(String userName, String userPwd);

    String encodeUserInfoByMd5(String userName, String userPwd, String md5Salt);
}
