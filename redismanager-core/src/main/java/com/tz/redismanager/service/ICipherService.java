package com.tz.redismanager.service;

/**
 * <p>密码service接口</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-02 20:06
 **/
public interface ICipherService {

    /**
     * 通过MD5加密用户数据
     *
     * @param userName
     * @param userPwd
     * @return
     */
    String encodeUserInfoByMd5(String userName, String userPwd);

    /**
     * 通过MD5加密用户数据
     *
     * @param userName
     * @param userPwd
     * @param md5Salt  加盐
     * @return
     */
    String encodeUserInfoByMd5(String userName, String userPwd, String md5Salt);
}
