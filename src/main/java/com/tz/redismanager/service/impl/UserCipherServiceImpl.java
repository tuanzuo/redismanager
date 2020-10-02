package com.tz.redismanager.service.impl;

import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.service.IUserCipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>用户密码service</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-10-02 20:07
 **/
@Service
public class UserCipherServiceImpl implements IUserCipherService {

    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    public String getUserEncodeInfo(String userName, String userPwd) {
        return this.getUserEncodeInfo(userName, userPwd, encryptConfig.getMd5Salt());
    }

    @Override
    public String getUserEncodeInfo(String userName, String userPwd, String md5Salt) {
        return DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", userName, userPwd, md5Salt).getBytes());
    }
}
