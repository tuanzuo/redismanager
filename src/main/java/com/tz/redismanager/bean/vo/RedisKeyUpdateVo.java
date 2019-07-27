package com.tz.redismanager.bean.vo;

import com.tz.redismanager.annotation.ConnectionId;

public class RedisKeyUpdateVo {
    @ConnectionId
    private String id;
    private String key;
    private String oldKey;
    /**
     * key类型：string,List,set,hash,zset
     */
    private String keyType;
    //过期时间
    private Long expireTime;
    private String stringValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getOldKey() {
        return oldKey;
    }

    public void setOldKey(String oldKey) {
        this.oldKey = oldKey;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
