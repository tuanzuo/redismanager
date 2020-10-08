package com.tz.redismanager.domain.vo;

import java.util.List;

/**
 * <p>用户列表返回对象</p>
 *
 * @author Administrator
 * @version 1.4.0
 * @time 2020-09-26 16:41
 **/
public class UserListResp {

    private Pagination pagination;

    private List<UserResp> list;

    private List<RoleVO> roles;

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

    public List<RoleVO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleVO> roles) {
        this.roles = roles;
    }
}
