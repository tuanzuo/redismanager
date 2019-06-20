package com.tz.redismanager.bean.vo;

import com.tz.redismanager.annotation.ConnectionId;

public class RedisValueQueryVo {
    @ConnectionId
    private String id;
    private String searchKey;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

}
