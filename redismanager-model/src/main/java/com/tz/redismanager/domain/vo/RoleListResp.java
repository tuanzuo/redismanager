package com.tz.redismanager.domain.vo;

import java.util.List;

/**
 * <p>角色列表返回对象</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-09-26 16:41
 **/
public class RoleListResp {

    private Pagination pagination;

    private List<RoleResp> list;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<RoleResp> getList() {
        return list;
    }

    public void setList(List<RoleResp> list) {
        this.list = list;
    }
}
