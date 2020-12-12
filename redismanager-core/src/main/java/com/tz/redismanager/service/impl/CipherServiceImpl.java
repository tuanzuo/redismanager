package com.tz.redismanager.service.impl;

import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.service.ICipherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>密码service</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-10-02 20:07
 **/
@Service
public class CipherServiceImpl implements ICipherService {

    @Autowired
    private EncryptConfig encryptConfig;

    @Override
    public String encodeUserInfoByMd5(String userName, String userPwd) {
        return this.encodeUserInfoByMd5(userName, userPwd, encryptConfig.getMd5Salt());
    }

    @Override
    public String encodeUserInfoByMd5(String userName, String userPwd, String md5Salt) {
        return DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", userName, userPwd, md5Salt).getBytes());
    }
}
