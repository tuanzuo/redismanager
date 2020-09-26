package com.tz.redismanager.domain.vo;

import java.util.List;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2020-09-26 16:41
 **/
public class UserListResp {

    private Pagination pagination;

    private List<UserResp> list;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<UserResp> getList() {
        return list;
    }

    public void setList(List<UserResp> list) {
        this.list = list;
    }
}
