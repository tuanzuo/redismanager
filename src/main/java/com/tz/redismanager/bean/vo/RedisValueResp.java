package com.tz.redismanager.bean.vo;


public class RedisValueResp {
    //过期时间
    private Long expireTime;
    //key对应的value
    private Object value;

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
