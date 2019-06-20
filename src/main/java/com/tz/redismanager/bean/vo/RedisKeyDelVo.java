package com.tz.redismanager.bean.vo;

import com.tz.redismanager.annotation.ConnectionId;

public class RedisKeyDelVo {
    @ConnectionId
    private String id;
    private String[] keys;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }
}
