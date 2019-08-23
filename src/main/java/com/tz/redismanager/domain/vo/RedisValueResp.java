package com.tz.redismanager.domain.vo;

/**
 * redis key对应value的返回数据
 *
 * @Since:2019-08-23 22:38:20
 * @Version:1.1.0
 */
public class RedisValueResp {
    /**
     * key类型：string,List,set,hash,zset
     */
    private String keyType;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * key对应的value
     */
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

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }
}
