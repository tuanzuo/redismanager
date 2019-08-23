package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * redis key查询value的VO
 * @Since:2019-08-23 22:35:31
 * @Version:1.1.0
 */
public class RedisValueQueryVO {
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
