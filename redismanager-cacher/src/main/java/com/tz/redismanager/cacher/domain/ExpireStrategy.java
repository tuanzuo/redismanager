package com.tz.redismanager.cacher.domain;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>过期策略</p>
 *
 * @author tuanzuo
 * @version 1.6.0
 * @time 2021-01-02 21:05
 **/
public enum ExpireStrategy {
    //写了之后多久过期
    EXPIRE_AFTER_WRITE,
    //最后一次访问了之后多久过期
    EXPIRE_AFTER_ACCESS,
    ;

    public ExpireStrategy getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (ExpireStrategy enm : ExpireStrategy.values()) {
            if (enm.toString().equals(code)) {
                return enm;
            }
        }
        return null;
    }


}
