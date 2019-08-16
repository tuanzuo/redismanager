package com.tz.redismanager.bean.vo;

import com.tz.redismanager.annotation.ConnectionId;
import org.hibernate.validator.constraints.NotEmpty;

public class RedisKeyDelVo {
    @NotEmpty(message = "id不能为空")
    @ConnectionId
    private String id;
    @NotEmpty(message = "keys不能为空")
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
