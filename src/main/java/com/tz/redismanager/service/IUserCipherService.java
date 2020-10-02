package com.tz.redismanager.service;

/**
 * <p>用户密码service接口</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-10-02 20:06
 **/
public interface IUserCipherService {

    public String getUserEncodeInfo(String userName, String userPwd);

    public String getUserEncodeInfo(String userName, String userPwd, String md5Salt);
}
