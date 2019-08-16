package com.tz.redismanager.bean.vo;

import com.tz.redismanager.annotation.ConnectionId;
import org.hibernate.validator.constraints.NotEmpty;

public class RedisValueQueryVo {
    @NotEmpty(message = "id不能为空")
    @ConnectionId
    private String id;
    @NotEmpty(message = "searchKey不能为空")
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
